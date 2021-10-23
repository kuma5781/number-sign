import React from 'react';
import { useAuthContext } from '../context/AuthContext';
import Top from './Top';
import TopError from './TopError';

const TopRouter: React.FC = () => {
  const { error } = useAuthContext() || {};
  return error ? (<TopError />) : (<Top />);
};

export default TopRouter;
