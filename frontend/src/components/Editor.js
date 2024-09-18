import React, { useState, useRef, useEffect } from "react";
import ReactQuill, { Quill } from "react-quill";
import "react-quill/dist/quill.snow.css"; 
import katex from "katex";
import "katex/dist/katex.min.css";
import { upload } from "@testing-library/user-event/dist/upload";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
window.katex = katex;

const FormulaBlot = Quill.import("formats/formula"); 

class CustomFormulaBlot extends FormulaBlot {
  static create(value) {
    const node = super.create(value);
    const formula = katex.renderToString(value, {
      throwOnError: false,
    });
    node.innerHTML = formula;
    return node;
  }

  static formats(node) {
    return node.innerText;
  }

  constructor(scroll, domNode) {
    super(scroll, domNode);
    this.quill = scroll.quill;
  }
}

CustomFormulaBlot.blotName = "formula";
CustomFormulaBlot.tagName = "span";

Quill.register(CustomFormulaBlot); 

const Editor = ({ lesson, setLesson, page, index}) => {
  const [content, setContent] = useState('');
  const axiosPrivate = useAxiosPrivate();
  const quillRef = useRef(null); // Создание рефа для Quill  
  const limit = 2000; // Ограничение на количество символов 

  useEffect(() => {
    setContent(lesson.pages[index].texts);
    const quill = quillRef.current.getEditor();
    const toolbar = quill.getModule('toolbar');

    // Custom image handler for toolbar
    toolbar.addHandler('image', () => {
      const input = document.createElement('input');
      input.setAttribute("type", "file");
      input.setAttribute("accept", "image/*");
      input.click();
      
      input.onchange = async () => {
        const file = input.files[0];
        if(file.type=== 'image/jpeg' || file.type=== 'image/png' || file.type=== 'image/gif'){
          const formData = new FormData();
          formData.append('image', file);
          formData.append('pageid', page)
          try {
            const response = await axiosPrivate.post(`/api/v1/page/image/add`,
              formData, { 
                headers: {
                    'Content-Type': 'multipart/form-data' 
                }
              }
            );
            const data = response.data;
            if (data) {
              const range = quill.getSelection();
              quill.insertEmbed(range.index, 'image', "http://localhost:8080/api/v1/page/files/"+data);
            } else {
              console.error('Image upload failed', data);
            }
          } catch (error) {
            console.error('Error uploading the image', error);
          }
        }
      };
    });
  }, [lesson.pages, index]);

  const modules = {
    toolbar: [
      ["bold", "italic", "underline", "strike"], // кнопки формата  
      ["blockquote", "code-block"],
      [{ list: "ordered" }, { list: "bullet" }],
      [{ script: "sub" }, { script: "super" }], // надстрочный/подстрочный текст  
      [{ header: [1, 2, 3, false] }],
      ["link", "formula", 'image', 'video'],
      [{ color: [] }, { background: [] }], // выпадающие списки с настройками  
      [{ align: [] }],
      ["clean"] // кнопка для удаления форматирования  
    ],
    clipboard: {
      matchVisual: false
    },
    
  };

  const handleChange = (html) => {
    const quill = quillRef.current.getEditor(); // Получение ссылки на редактор 
    if (quill.getLength() <= limit) {
      setContent(html); // Обновление контента  
      lesson.pages[index].texts = html;
      setLesson(lesson);
    } else if (quill.getLength() > limit) {
      quill.deleteText(limit, quill.getLength());
      setContent(quill.getText());
      lesson.pages[index].texts = quill.getText();
      setLesson(lesson);
    }
  };

  return (
      <ReactQuill
        ref={quillRef}
        theme="snow"
        style={{ flexBasis: "100%",  display: "flex", flexDirection: "column", }}
        formats={['header', 'font', 'size', 'bold', 'italic', 'underline', 'strike', 'blockquote', 'list', 'bullet', 'indent', 'link', 'image', 'video', 'formula']}
        placeholder="Write something amazing..."
        modules={modules}
        bounds={".app"}
        onChange={handleChange}
        value={content}
      />
  );
};

export default Editor;
