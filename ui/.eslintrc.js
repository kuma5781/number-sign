module.exports = {
  env: {
    browser: true,
    es2021: true,
    node: true,
  },
  root: true,
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaFeatures: {
      jsx: true,
    },
    ecmaVersion: 12,
    sourceType: 'module',
  },
  plugins: [
    '@typescript-eslint',
    'import',
    'jsx-a11y',
    'react',
  ],
  extends: [
    'eslint:recommended',
    'airbnb',
    'plugin:@typescript-eslint/recommended',
  ],
  settings: {
    'import/resolver': {
      node: {
        // Typescriptファイル内のimportを許可
        extensions: [
          '.ts',
          '.tsx',
        ],
      },
    },
  },
  rules: {
    // tsxにjsxの記法を許可する
    'react/jsx-filename-extension': [
      'error',
      {
        extensions: ['.jsx', '.tsx'],
      },
    ],
    // labelをオプションなしでもOK
    'jsx-a11y/label-has-associated-control': 'off',
    // buttonをオプションなしでもOK
    'react/button-has-type': 'off',
    // typescriptでReactのimportを許可
    'no-use-before-define': 'off',
    '@typescript-eslint/no-use-before-define': ['error'],
    // named exportを許可
    'import/prefer-default-export': 'off',
    //
    'import/extensions': [
      'error',
      // 以下の拡張子はimport時必ずなしにする
      {
        js: 'never',
        jsx: 'never',
        ts: 'never',
        tsx: 'never',
      },
    ],
    'react/prop-types': 'off',
  },
};
