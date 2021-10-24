import React, { useLayoutEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import marked from 'marked';
import './NoteEditer.css';

const backendUrl = process.env.REACT_APP_BACKEND_URL as string;

const NoteEditer: React.FC = () => {
  const { noteId } = useParams<{noteId: string}>();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [markdown, setMarkdown] = useState('');
  const [savedMessage, setSavedMessage] = useState('');
  useLayoutEffect(() => {
    fetch(`${backendUrl}/note/${noteId}`, {
      method: 'GET',
    }).then((response) => response.json())
      .then((data) => {
        setTitle(data.title);
        setContent(data.content);
        setMarkdown(data.content);
      })
      .catch((err) => console.error(err));
  }, [noteId]);
  const noteSubmit = async (event: any) => {
    event.preventDefault();
    const { editTitle, editContent } = event.target.elements;
    fetch(`${backendUrl}/note/title-and-content/${noteId}`, {
      headers: {
        'Content-Type': 'application/json',
      },
      method: 'PUT',
      body: JSON.stringify({ title: editTitle.value, content: editContent.value }),
    }).then((response) => {
      const now = new Date();
      const year = now.getFullYear();
      const month = now.getMonth() + 1;
      const date = now.getDate();
      const hour = now.getHours();
      const min = now.getMinutes();
      const sec = now.getSeconds();
      const msec = now.getMilliseconds();
      setSavedMessage(`保存しました[${year}年${month}月${date}日 ${hour}:${min}:${sec}:${msec}]`);
      console.log(response);
    }).catch((err) => console.error(err));
  };
  return (
    <div className="main">
      <div className="editer">
        <form onSubmit={noteSubmit}>
          <div>
            <input name="editTitle" type="text" className="input-title" defaultValue={title} placeholder="" />
          </div>
          <div>
            <textarea
              id="content"
              name="editContent"
              className="input-content"
              defaultValue={content}
              onChange={(e) => setMarkdown(e.target.value)}
            />
          </div>
          <div className="noteediter-footer">
            <button className="noteediter-save-button">保存</button>
            <a href={`/view/${noteId}`} className="noteediter-to-view-link">戻る</a>
            {savedMessage && <span className="noteediter-saved-message">{savedMessage}</span>}
          </div>
        </form>
      </div>
      <div className="preview markdown">
        <span dangerouslySetInnerHTML={{ __html: marked(markdown) }} />
      </div>
    </div>
  );
};

export default NoteEditer;