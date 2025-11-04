import React, { useState, useEffect } from 'react';
import Editor from '@monaco-editor/react';

function CodeEditor({ 
  value, 
  onChange, 
  language = 'python', 
  height = '400px',
  readOnly = false 
}) {
  const [editorTheme, setEditorTheme] = useState('vs-light');

  useEffect(() => {
    // Check for dark mode preference
    const isDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    setEditorTheme(isDark ? 'vs-dark' : 'vs-light');
  }, []);

  const handleEditorChange = (value) => {
    if (onChange) {
      onChange(value);
    }
  };

  const getLanguageId = (lang) => {
    const languageMap = {
      'python': 'python',
      'java': 'java',
      'cpp': 'cpp',
      'javascript': 'javascript',
      'c': 'c'
    };
    return languageMap[lang.toLowerCase()] || 'python';
  };

  return (
    <div className="border border-gray-300 rounded-lg overflow-hidden">
      <Editor
        height={height}
        language={getLanguageId(language)}
        value={value}
        onChange={handleEditorChange}
        theme={editorTheme}
        options={{
          minimap: { enabled: false },
          scrollBeyondLastLine: false,
          fontSize: 14,
          lineNumbers: 'on',
          roundedSelection: false,
          scrollbar: {
            vertical: 'auto',
            horizontal: 'auto',
          },
          automaticLayout: true,
          readOnly: readOnly,
          wordWrap: 'on',
        }}
      />
    </div>
  );
}

export default CodeEditor;
