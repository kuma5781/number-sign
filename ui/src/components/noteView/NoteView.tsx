import React, { useLayoutEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import marked from 'marked';
import './NoteView.css';
import './Markdown.css';
import Side from '../side/Side';
import { useAuthContext } from '../context/AuthContext';

const backendUrl = process.env.REACT_APP_BACKEND_URL as string;

const NoteView: React.FC = () => {
  const { noteId } = useParams<{noteId: string}>();
  const userId = 1;
  const { userInfo } = useAuthContext() || {};
  const [title, setTitle] = useState('');
  const [markdown, setMarkdown] = useState('');
  useLayoutEffect(() => {
    fetch(`${backendUrl}/note/${noteId}`, {
      method: 'GET',
    }).then((response) => response.json())
      .then((data) => {
        setTitle(data.title);
        setMarkdown(data.content);
      })
      .catch((err) => console.error(err));
  }, [noteId]);
  return (
    <div className="noteview">
      <Side />
      <div className="noteview-main">
        <a href={`/edit/${noteId}`}>
          <div className="noteview-edit-link">
            <img alt="pencil" src={`${process.env.PUBLIC_URL}/img/pencil.png`} className="noteview-pencil" />
          </div>
        </a>
        <h1>{title}</h1>
        <div className="markdown">
          <span dangerouslySetInnerHTML={{ __html: marked(markdown) }} />
        </div>
      </div>
    </div>
  );
};

export default NoteView;
