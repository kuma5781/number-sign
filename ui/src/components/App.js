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
