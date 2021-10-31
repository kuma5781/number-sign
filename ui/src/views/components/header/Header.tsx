import React, {
  useRef, useState, useCallback,
} from 'react';
import { Link, useHistory } from 'react-router-dom';
import { useAuthContext } from '../context/AuthContext';
import { auth } from '../../../firebase';
import './Header.css';

const Header: React.FC = () => {
  const { userInfo } = useAuthContext() || {};
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const popupRef = useRef<HTMLDivElement | null>(null);
  const buttonRef = useRef<HTMLButtonElement | null>(null);
  const history = useHistory();
  const handleLogout = () => {
    auth.signOut();
    history.push('/login');
  };

  const openButtonClickHandler = () => {
    if (isOpen) {
      setIsOpen(false);
      return;
    }
    setIsOpen(true);
    addOutsideClickHandler();
  };

  const removeOutsideClickHandler = useCallback(() => {
    document.querySelector('root')?.removeEventListener('click', () => {
      setIsOpen(false);
      removeOutsideClickHandler();
    });
  }, []);

  const addOutsideClickHandler = useCallback(() => {
    document.querySelector('#root')?.addEventListener('click', (e) => {
      if (buttonRef.current?.contains(e.target as Node)) return;
      if (popupRef.current?.contains(e.target as Node)) return;
      setIsOpen(false);
      removeOutsideClickHandler();
    });
  }, []);

  return (
    <div className="header-container">
      <div className="header-inner-container">
        <div className="header-subject">
          <Link to="/" className="header-subject-to-top">
            Number Sign
          </Link>
        </div>
        <div>
          { userInfo.name && (
          <button onClick={openButtonClickHandler} className="header-box-user-info" ref={buttonRef}>
            <div className="header-user-name-text">{userInfo.name}</div>
            <img className="header-user-name-icon" src={`${process.env.PUBLIC_URL}/img/triangle.svg`} alt="" />
          </button>
          )}
        </div>
      </div>
      <div className={`${isOpen ? 'header-popup-open' : 'header-popup-close'}`} ref={popupRef}>
        <button onClick={handleLogout} className="header-button-sign-out">ログアウト</button>
      </div>
    </div>
  );
};

export default Header;
