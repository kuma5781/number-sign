import React, { useLayoutEffect, useState } from 'react';
import { useAuthContext } from '../context/AuthContext';
import './Side.css';

const backendUrl = process.env.REACT_APP_BACKEND_URL as string;

const Side: React.FC = () => {
  const { userInfo } = useAuthContext() || {};

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

  // フォルダメニューを開く
  const openMenu = async (event: any) => {
    event.preventDefault();
    let { target } = event;
    while (!target.id.match(/folder_box/)) {
      target = target.parentNode;
    }
    const openedMenus = document.querySelectorAll('.side-folder-menu.side-folder-menu-open');
    openedMenus.forEach((menu) => {
      menu?.classList.remove('side-folder-menu-open');
    });
    const folderMenu = document.querySelector(`#${target.id} .side-folder-menu`);
    folderMenu?.classList.add('side-folder-menu-open');
  };

  // ノート作成フォームを開く
  const openNewNoteForm = async (event: any) => {
    event.preventDefault();
    let { target } = event;
    while (!target.id.match(/folder_box/)) {
      target = target.parentNode;
    }
    const folderMenu = document.querySelector(`#${target.id} .side-folder-menu`);
    folderMenu?.classList.remove('side-folder-menu-open');
    const newNoteForm = document.querySelector(`#${target.id} .side-new-note-form`);
    newNoteForm?.classList.add('side-new-note-form-open');
  };

  document.addEventListener('click', (e: any) => {
    // 開いているフォルダメニュー以外がクリックされたとき, フォルダメニューを閉じる
    if (!e.target.closest('.side-folder-menu.side-folder-menu-open')) {
      const openedMenus = document.querySelectorAll('.side-folder-menu.side-folder-menu-open');
      openedMenus.forEach((menu) => {
        menu?.classList.remove('side-folder-menu-open');
      });
    }
    // 開いているノート作成フォーム以外がクリックされたとき, ノート作成フォームを閉じる
    if (!e.target.closest('.side-new-note-form.side-new-note-form-open') && !e.target.closest('.side-open-new-note-form')) {
      const openedForms = document.querySelectorAll('.side-new-note-form.side-new-note-form-open');
      openedForms.forEach((form) => {
        form?.classList.remove('side-new-note-form-open');
      });
    }
  });

  // エンターキー押下でノート作成
  const createNote = async (event: any) => {
    if (event.keyCode === 13) {
      event.preventDefault();
      const { parentFolderId, noteTitle } = event.target.parentNode;
      fetch(`${backendUrl}/note`, {
        headers: {
          'Content-Type': 'application/json',
        },
        method: 'POST',
        body: JSON.stringify({
          user_id: userInfo.id,
          title: noteTitle.value,
          content: '',
          parent_folder_id: Number(parentFolderId.value),
        }),
      }).then((response) => {
        if (response.ok) {
          console.log(response);
          getFoldersNotes();
        } else {
          console.error(response);
        }
      });
    }
  };

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
    folderNameBox.oncontextmenu = openMenu;
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

    // フォルダメニュー
    const folderMenu = document.createElement('div');
    folderMenu.classList.add('side-folder-menu');
    folderBox.appendChild(folderMenu);

    const newNote = document.createElement('button');
    newNote.textContent = '新しいノート';
    newNote.classList.add('side-open-new-note-form');
    newNote.onclick = openNewNoteForm;
    folderMenu.appendChild(newNote);

    const newFolder = document.createElement('button');
    newFolder.textContent = '新しいフォルダ';
    folderMenu.appendChild(newFolder);

    // ノート作成フォーム
    const newNoteForm = document.createElement('form');
    newNoteForm.onsubmit = createNote;
    newNoteForm.classList.add('side-new-note-form');
    folderBox.appendChild(newNoteForm);

    const noteImg = document.createElement('img');
    noteImg.setAttribute('alt', 'note');
    noteImg.setAttribute('src', `${process.env.PUBLIC_URL}/img/note.png`);
    noteImg.classList.add('side-note');
    newNoteForm.appendChild(noteImg);

    const inputParentFolerId = document.createElement('input');
    inputParentFolerId.name = 'parentFolderId';
    inputParentFolerId.type = 'hidden';
    inputParentFolerId.value = folder.id;
    newNoteForm.appendChild(inputParentFolerId);

    const inputNoteTitle = document.createElement('input');
    inputNoteTitle.name = 'noteTitle';
    inputNoteTitle.type = 'text';
    inputNoteTitle.classList.add('side-input-note-title');
    inputNoteTitle.onkeydown = createNote;
    newNoteForm.appendChild(inputNoteTitle);

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

    // ノート画像
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
    (document.getElementById('notes') as HTMLElement).innerHTML = '';
    folders.forEach((folder) => {
      addFolderElment(folder);
    });
    notes.forEach((note) => {
      addNoteElment(note);
    });
  };

  // ログイン中のユーザのフォルダとノートを全て取得して描画
  const getFoldersNotes = async () => {
    fetch(`${backendUrl}/folder/${userInfo.id}`, {
      method: 'GET',
    }).then((response) => response.json())
      .then((data) => {
        const { folders, notes } = data;
        createSide(folders, notes);
      })
      .catch((err) => console.error(err));
  };
  useLayoutEffect(() => {
    getFoldersNotes();
  }, [userInfo.id]);

  return (
    <div id="side" className="side">
      <p className="side-workspace-title">{`${userInfo.name}さんのワークスペース`}</p>
      <div id="notes" className="side-notes" />
    </div>
  );
};

export default Side;
