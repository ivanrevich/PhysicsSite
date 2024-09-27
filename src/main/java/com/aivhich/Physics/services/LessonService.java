package com.aivhich.Physics.services;

import com.aivhich.Physics.auth.models.User;
import com.aivhich.Physics.dto.request.*;
import com.aivhich.Physics.dto.response.CourseResponse;
import com.aivhich.Physics.models.*;
import com.aivhich.Physics.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class LessonService {
    private static final String UPLOAD_PATH = "src/main/resources/static/files/";

    @Autowired
    LevelRepository levelRepository;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PagesRepository pagesRepository;
    @Autowired
    FormsRepository formsRepository;
    private Long lastPassed;


    public Lesson createNewLesson(CreateLessonRequest request) {
        Lesson lesson = Lesson.builder().title(request.getTitle()).build();

        Level lvl = levelRepository.findById(request.getLevel()).orElseThrow();
        lesson.setLevel(lvl);
        lesson.setStepId((long) lvl.getLessons().size()+1);
        lessonRepository.save(lesson);
        if (lesson.getLevel() != null) return lesson;
        return null;
    }

    public void updatePage(UUID id, String htmltext, int points) {
        Page page = pagesRepository.findById(id).orElseThrow();
        page.setTexts(htmltext);
        page.setPoints(points);
        pagesRepository.save(page);
    }

    public Level createNewLevel(CreateLevelRequest createLevelRequest) {
        List<Level> levels = levelRepository.findAll();
        Level newLevel = Level.builder()
                .title(createLevelRequest.getTitle())
                .course(Course.PHYSICS)
                .stepId((long) (levels.size() + 1))
                .lessons(Collections.emptyList())
                .build();
        levelRepository.save(newLevel);
        return newLevel;
    }

    public List<Level> getLevels() {
        return levelRepository.findAll();
    }

    public Lesson getLesson(UUID id) {
        return lessonRepository.findById(id).orElse(null);
    }

    public void deleteLvl(UUID id) {
        Level level = levelRepository.findById(id).orElseThrow();
        List<Level> levels = levelRepository.findAll().stream()
                .filter(it -> it.getStepId() > level.getStepId())
                .peek(it -> it.setStepId(it.getStepId() - 1))
                .toList();

        Set<User> users = new HashSet<>();
        Set<Form> forms = new HashSet<>();

        for (Lesson lesson : level.getLessons()) {
            for (Page page : lesson.getPages()) {
                // Удаляем ссылки на форму из страниц
                forms.add(page.getForm());

                // Удаляем ссылки на страницы у пользователей
                for (User user : page.getUsersPassed()) {
                    Set<Page> pages = user.getPassedPages();
                    pages.remove(page);
                    user.setPassedPages(pages);

                    Set<Form> doCorr = user.getUserDoCorrectly();
                    Set<Form> doNotCorr = user.getUserDoNotCorrectly();
                    doCorr.remove(page.getForm());
                    doNotCorr.remove(page.getForm());
                    user.setUserDoNotCorrectly(doNotCorr);
                    user.setUserDoCorrectly(doCorr);

                    userRepository.save(user);
                }
                page.setForm(null); // Или удалите страницу, если это необходимо
                pagesRepository.save(page); // Сохраните изменения
            }
            users.addAll(lesson.getUsersPassed());
        }

        formsRepository.deleteAll(forms); // Удаляем формы после очистки ссылок

        for (User user : users) {
            Set<Lesson> lessons = user.getPassedLessons();
            level.getLessons().forEach(lessons::remove);
            user.setPassedLessons(lessons);
            userRepository.save(user);
        }

        levelRepository.deleteById(id);
        levelRepository.saveAll(levels);
    }

    public void deleteLesson(UUID id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow();
        List<Lesson> lessons = ((lessonRepository.findAll().stream()
                .filter(it->it.getLevel().getId()==lesson.getLevel().getId()))
                .filter(it->it.getStepId()>lesson.getStepId())).peek(it->it.setStepId(it.getStepId()-1)).toList();




        Set<Form> forms = new HashSet<>();

        for (Page page : lesson.getPages()) {
            // Удаляем ссылки на форму из страниц
            forms.add(page.getForm());

            // Удаляем ссылки на страницы у пользователей
            for (User user : page.getUsersPassed()) {
                Set<Page> pages = user.getPassedPages();
                pages.remove(page);
                user.setPassedPages(pages);

                Set<Form> doCorr = user.getUserDoCorrectly();
                Set<Form> doNotCorr = user.getUserDoNotCorrectly();
                doCorr.remove(page.getForm());
                doNotCorr.remove(page.getForm());
                user.setUserDoNotCorrectly(doNotCorr);
                user.setUserDoCorrectly(doCorr);

                userRepository.save(user);
            }
            page.setForm(null); // Или удалите страницу, если это необходимо
            pagesRepository.save(page); // Сохраните изменения
        }

        for(User user : lesson.getUsersPassed()){
            Set<Lesson> userPassedLessons = user.getPassedLessons();
            userPassedLessons.remove(lesson);
            user.setPassedLessons(userPassedLessons);
            userRepository.save(user);
        }

        formsRepository.deleteAll(forms);

        lessonRepository.deleteById(id);
        lessonRepository.saveAll(lessons);
    }

    public Page createNewPage(UUID lessonId, InfoType infoType, Long stepid) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();

        Page page = Page.builder()
                .lesson(lesson)
                .stepId(stepid)
                .infoType(infoType)
                .build();
        List<Page> pages = ((pagesRepository.findAll().stream()
                .filter(it->(it.getLesson().getId()==page.getLesson().getId())&&it.getId()!=page.getId()))
                .filter(it->it.getStepId()>=page.getStepId())).peek(it->it.setStepId(it.getStepId()+1)).toList();
        pagesRepository.saveAll(pages);

        if (infoType == InfoType.TASK) {
            Form form = Form.builder()
                    .pages(List.of(page))
                    .formType(FormType.CALC)
                    .usersDoCorrectly(Set.of())
                    .usersDoNotCorrectly(Set.of())
                    .answers(Set.of())
                    .rightAnswers(Set.of())
                    .build();
            formsRepository.save(form);
            page.setForm(form);
        }
        pagesRepository.save(page);
        return page;
    }

    public void deletePage(UUID id) {
        Page page = pagesRepository.findById(id).orElseThrow();
        List<Page> pages = ((pagesRepository.findAll().stream()
                .filter(it->(it.getLesson().getId()==page.getLesson().getId())))
                .filter(it->it.getStepId()>page.getStepId())).peek(it->it.setStepId(it.getStepId()-1)).toList();
        pagesRepository.saveAll(pages);
        pagesRepository.deleteById(id);
    }

    public void updateLessonTitle(UUID id, String title) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow();
        lesson.setTitle(title);
        lessonRepository.save(lesson);
    }

    public List<Page> getAllPages(){
        return pagesRepository.findAll();
    }

    public List<Lesson> getAllLesson(){
        return lessonRepository.findAll();
    }

    public List<Level> getAllLvls(){
        return levelRepository.findAll();
    }

    public List<Form> getAllForms(){
        return formsRepository.findAll();
    }

    public void updateForm(UpdateFormRequest updateFormRequest) {
        Page page = pagesRepository.findById(updateFormRequest.getPageId()).orElseThrow();
        page.setTexts(updateFormRequest.getHtmlText());

        Form form = formsRepository.findById(updateFormRequest.getFormId()).orElseThrow();
        form.setFormType(updateFormRequest.getFormType());
        form.setRightAnswers(updateFormRequest.getRight());
        form.setAnswers(updateFormRequest.getAnswers());
        formsRepository.save(form);
        page.setForm(form);
        pagesRepository.save(page);//
    }

    public CourseResponse getPassedLessons(Principal connectedUser, Course course) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (user.getCourses().contains(course)) {
            List<Lesson> passed = user
                    .getPassedLessons()
                    .stream()
                    .sorted(Comparator.comparingLong(Lesson::getStepId))
                    .toList();

            Long lastPassed = 0L;
            try {
                lastPassed = passed.get(passed.size() - 1)
                        .getStepId();
            } catch (Exception e) {
            }

            Set<Lesson> lessons = user.getPassedLessons();
            Set<Page> pages = user.getPassedPages();
            Set<LessonProgressDto> lessonProgressDtos = new HashSet<>(Collections.emptySet());

            lessons.forEach(it -> {
                lessonProgressDtos.add(LessonProgressDto
                        .builder()
                        .lesson(it.getId())
                        .points(pages
                                .stream()
                                .filter(p -> p.getLesson().getId() == it.getId())
                                .mapToLong(Page::getPoints)
                                .sum())
                        .build());
            });
            return CourseResponse.builder().lessonProgressSet(lessonProgressDtos).passed(passed).LastPassedId(lastPassed).build();
        }
        return CourseResponse.builder().build();
    }

    public Long getWeekPoints(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        long epochMilli = Instant.now().toEpochMilli() - (LocalDate.now().with(java.time.DayOfWeek.MONDAY).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
        return user.getPassedPages()
                .stream()
                .filter(it -> it.getTimestamp() > epochMilli)
                .map(Page::getPoints)
                .flatMapToLong(it -> LongStream.of(it.longValue()))
                .sum();
    }

    public Long updateProgress(Principal principal, UpdateProgress updRequest) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        Set<Form> passedRightForms = user.getUserDoCorrectly();
        Set<Form> notRightPassedForms = user.getUserDoNotCorrectly();

        Set<Page> passedPages = user.getPassedPages();
        long epochMilli = Instant.now().toEpochMilli();
        List<Page> newPassedPages = pagesRepository.findAllById(updRequest.getPassedPages()).stream().peek(it -> it.setTimestamp(epochMilli)).toList();
        passedPages.addAll(newPassedPages);

        List<Form> newRightForms = formsRepository.findAllById(updRequest.getRightForms());
        passedRightForms.addAll(newRightForms);

        if (!updRequest.getRightForms().isEmpty()) {
            List<Form> newNotRightForm = passedPages
                    .stream()
                    .map(Page::getForm).filter(form ->
                            form != null && updRequest.getRightForms().contains(form.getId())).toList();
            notRightPassedForms.addAll(newNotRightForm);
        }

        /*fuck code but is code*/
        Set<Lesson> passedLessons = user.getPassedLessons();
        Lesson newLesson = pagesRepository.findById(updRequest.getPassedPages().get(0)).orElseThrow().getLesson();
        passedLessons.add(newLesson);
        user.setPassedLessons(passedLessons);
        /*that incorrect*/


        user.setUserDoCorrectly(passedRightForms);
        user.setUserDoNotCorrectly(notRightPassedForms);
        user.setPassedPages(passedPages);

//        newNotRightForm.forEach(it -> {
//            Set<User> passed = it.getUsersDoNotCorrectly();
//            passed.add(user);
//            it.setUsersDoNotCorrectly(passed);
//        });
//        newRightForms.forEach(it -> {
//            Set<User> passed = it.getUsersDoCorrectly();
//            passed.add(user);
//            it.setUsersDoCorrectly(passed);
//        });
//
//        passedPages.forEach(it -> {
//            Set<User> passed = it.getUsersPassed();
//            passed.add(user);
//            it.setUsersPassed(passed);
//        });
        Long outPoints = newPassedPages
                .stream()
                .flatMapToLong(
                        page -> {
                            if (page.getInfoType() == InfoType.THEORY) {
                                return LongStream.of(page.getPoints());
                            } else {
                                if (newRightForms.stream().map(Form::getId).toList().contains(page.getForm().getId())) {
                                    return LongStream.of(page.getPoints());
                                }
                            }
                            return LongStream.of(0L);
                        }
                ).sum();
        Long maxPoints = newPassedPages
                .stream()
                .flatMapToLong(
                        page -> {
                            if (page.getInfoType() == InfoType.THEORY) {
                                return LongStream.of(page.getPoints());
                            } else {
                                return LongStream.of(page.getPoints());
                            }
                        }
                ).sum();

        if (maxPoints * 0.5 <= outPoints) {
            formsRepository.saveAll(passedRightForms);
            pagesRepository.saveAll(passedPages);
            userRepository.save(user);
        }
        return outPoints;
    }

    public String addImage(MultipartFile file, String pageid) {

        String[] fileNames = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String name = pageid+Instant.now().toEpochMilli()+"."+fileNames[1];
        try {
            Path path = Paths.get(UPLOAD_PATH+name);
            Files.write(path, file.getBytes());
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
