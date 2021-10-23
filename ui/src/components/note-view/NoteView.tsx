import React, { useLayoutEffect } from 'react';
import './NoteView.css';

const backendUrl = process.env.REACT_APP_BACKEND_URL as string;

const NoteView: React.FC = () => {
  const userId = 1;
  const addFolderElment = async (folder: any) => {
    // フォルダ要素
    const folderBox = document.createElement('div');
    folderBox.classList.add('folder-box');

    // フォルダ名
    const folderNameBox = document.createElement('p');
    folderNameBox.classList.add('folder-name');
    folderNameBox.textContent = folder.name;
    folderBox.appendChild(folderNameBox);

    // 子フォルダ, 子ノートを格納する要素
    const childFoldersBox = document.createElement('div');
    childFoldersBox.setAttribute('id', `child_folders_box${folder.id}`);
    childFoldersBox.classList.add('child-folder-box');
    folderBox.appendChild(childFoldersBox);

    // フォルダ要素をHTMLに追加
    if (folder.parent_folder_id) {
      document.getElementById(`child_folders_box${folder.parent_folder_id}`)?.appendChild(folderBox);
    } else {
      document.getElementById('side')?.appendChild(folderBox);
    }
  };
  const addNoteElment = async (note: any) => {
    // ノート要素
    const noteBox = document.createElement('div');
    noteBox.classList.add('note-box');

    // ノートタイトル
    const noteTitleBox = document.createElement('p');
    noteTitleBox.classList.add('note-title');
    noteTitleBox.textContent = note.title;
    noteBox.appendChild(noteTitleBox);

    // ノート要素をHTMLに追加
    if (note.parent_folder_id) {
      document.getElementById(`child_folders_box${note.parent_folder_id}`)?.appendChild(noteBox);
    } else {
      document.getElementById('side')?.appendChild(noteBox);
    }
  };
  const createSide = async (folders: never[], notes: never[]) => {
    document.getElementById('side')!.innerHTML = '';
    folders.forEach((folder) => {
      addFolderElment(folder);
    });
    notes.forEach((note) => {
      addNoteElment(note);
    });
  };
  useLayoutEffect(() => {
    fetch(`${backendUrl}/folder/${userId}`, {
      method: 'GET',
    }).then((response) => response.json())
      .then((data) => {
        const { folders, notes } = data;
        createSide(folders, notes);
        console.log(folders);
        console.log(notes);
      })
      .catch((err) => console.error(err));
  }, [userId]);
  return (
    <div>
      <div id="side" />
      <img alt="arrow" src={`${process.env.PUBLIC_URL}/img/arrow.png`} />
      <img alt="folder" src={`${process.env.PUBLIC_URL}/img/folder.png`} />
      <img alt="note" src={`${process.env.PUBLIC_URL}/img/note.png`} />
      <img alt="pencil" src={`${process.env.PUBLIC_URL}/img/pencil.png`} />
    </div>
  );
};

export default NoteView;
