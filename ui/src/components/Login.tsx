// import React from 'react';
// import {
//   GoogleButton,
//   IAuthorizationOptions,
// } from 'react-google-oauth2';

// const Login: React.FC = () => {
//   const options: IAuthorizationOptions = {
//     clientId: (process.env.REACT_APP_CLIENT_ID as string),
//     redirectUri: 'http://localhost:3000/login/oauth2/code/google',
//     scopes: ['openid', 'profile', 'email'],
//     includeGrantedScopes: true,
//     accessType: 'offline',
//   };

//   return (
//     <GoogleButton
//       options={options}
//       apiUrl="http://localhost:5000/google_login"
//       defaultStyle
//     >
//       ログイン
//     </GoogleButton>
//   );
// };

// export default Login;

import React, { Component } from 'react';

class App extends Component {
  state = { isSignedIn: null };

  componentDidMount() {
    window.gapi.load('client:auth2', () => {
      window.gapi.client.init({
        clientId: "705906324235-1tguef73fght9qvgfpr48dk79itvh9dd.apps.googleusercontent.com",
        scope: "email"
      }).then(() => {
        const auth = window.gapi.auth2.getAuthInstance();

        this.setState({ isSignedIn: auth.isSignedIn.get() });
      });
    });
  }

  renderAuth() {
    if (this.state.isSignedIn === null) {
      return <div>i dont know your google account</div>
    } else if (this.state.isSignedIn) {
      return <div>login with google!!</div>
    } else {
      return <div>I can not see your google account!!</div>
    }
  }

  loginWithGoogle = () => {
    window.gapi.auth2.getAuthInstance().signIn();
  }

  logoutFromGoogle = () => {
    window.gapi.auth2.getAuthInstance().signOut();
  }

  render() {
    return (
      <div>
        {this.renderAuth()}
        <button onClick={this.loginWithGoogle}>
          login with google
        </button>
        <button onClick={this.logoutFromGoogle}>
          logout from google
        </button>
      </div>
    );
  }
}

export default App;
