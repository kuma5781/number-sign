import React, { useLayoutEffect } from 'react';
import { useAuthContext } from '../../components/context/AuthContext';
import './Side.css';

const backendUrl = process.env.REACT_APP_BACKEND_URL as string;

// TODO: any型を使わない
// フォルダを開いたり閉じたりする
const folderOpenToggle = async (event: any) => {
  event.preventDefault();
  let { target } = event;
  while (!target.id.match(/folder_box/)) {
    target = target.parentNode;
  }
  const allowImg = document.querySelector(`#${target.id} .side-allow`);
  allowImg?.classList.toggle('side-allow-close');
  const childFoldersBox = document.querySelector(`#${target.id} .side-child-folder-box`);
  childFoldersBox?.classList.toggle('side-child-folder-box-close');
};

const Side: React.FC = () => {
  const { userInfo } = useAuthContext() || {};
  // TODO: any型を使わない
  const addFolderElment = async (folder: any) => {
    // フォルダ要素
    const folderBox = document.createElement('div');
    folderBox.setAttribute('id', `folder_box${folder.id}`);
    folderBox.classList.add('side-folder-box');

    // フォルダ名や画像を含む要素
    const folderNameBox = document.createElement('div');
    folderNameBox.classList.add('side-folder-name-box');
    folderNameBox.onclick = folderOpenToggle;
    folderBox.appendChild(folderNameBox);

    // 矢印画像
    const allowImg = document.createElement('img');
    allowImg.setAttribute('alt', 'arrow');
    allowImg.setAttribute('src', `${process.env.PUBLIC_URL}/img/arrow.png`);
    allowImg.classList.add('side-allow');
    folderNameBox.appendChild(allowImg);

    // フォルダ画像
    const folderImg = document.createElement('img');
    folderImg.setAttribute('alt', 'folder');
    folderImg.setAttribute('src', `${process.env.PUBLIC_URL}/img/folder.png`);
    folderImg.classList.add('side-folder');
    folderNameBox.appendChild(folderImg);

    // フォルダ名
    const folderName = document.createElement('p');
    folderName.classList.add('side-folder-name');
    folderName.textContent = folder.name;
    folderNameBox.appendChild(folderName);

    // 子フォルダ, 子ノートを格納する要素
    const childFoldersBox = document.createElement('div');
    childFoldersBox.setAttribute('id', `child_folders_box${folder.id}`);
    childFoldersBox.classList.add('side-child-folder-box');
    folderBox.appendChild(childFoldersBox);

    // フォルダ要素をHTMLに追加
    if (folder.parent_folder_id) {
      document.getElementById(`child_folders_box${folder.parent_folder_id}`)?.appendChild(folderBox);
    } else {
      document.getElementById('notes')?.appendChild(folderBox);
    }
  };
  // TODO: any型を使わない
  const addNoteElment = async (note: any) => {
    // ノート要素
    const noteBox = document.createElement('div');
    noteBox.classList.add('side-note-box');

    // ノートのリンク
    const noteLink = document.createElement('a');
    noteLink.setAttribute('href', `/view/${note.id}`);
    noteLink.classList.add('side-note-link');
    noteBox.appendChild(noteLink);

    // フォルダ画像
    const noteImg = document.createElement('img');
    noteImg.setAttribute('alt', 'note');
    noteImg.setAttribute('src', `${process.env.PUBLIC_URL}/img/note.png`);
    noteImg.classList.add('side-note');
    noteLink.appendChild(noteImg);

    // ノートタイトル
    const noteTitleBox = document.createElement('p');
    noteTitleBox.classList.add('side-note-title');
    noteTitleBox.textContent = note.title;
    noteLink.appendChild(noteTitleBox);

    // ノート要素をHTMLに追加
    if (note.parent_folder_id) {
      document.getElementById(`child_folders_box${note.parent_folder_id}`)?.appendChild(noteBox);
    } else {
      document.getElementById('notes')?.appendChild(noteBox);
    }
  };
  const createSide = async (folders: never[], notes: never[]) => {
    document.getElementById('notes')!.innerHTML = '';
    folders.forEach((folder) => {
      addFolderElment(folder);
    });
    notes.forEach((note) => {
      addNoteElment(note);
    });
  };
  useLayoutEffect(() => {
    fetch(`${backendUrl}/folder/${userInfo.id}`, {
      method: 'GET',
    }).then((response) => response.json())
      .then((data) => {
        const { folders, notes } = data;
        createSide(folders, notes);
      })
      .catch((err) => console.error(err));
  }, [userInfo.id]);
  return (
    <div id="side" className="side">
      <p className="side-workspace-title">{`${userInfo.name}さんのワークスペース`}</p>
      <div id="notes" className="side-notes" />
    </div>
  );
};

export default Side;
