import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import api from '../services/api';
import CodeEditor from '../components/CodeEditor';

function ProblemDetail() {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  
  const [problem, setProblem] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [code, setCode] = useState('');
  const [language, setLanguage] = useState('python');
  const [submitting, setSubmitting] = useState(false);
  const [submissionResult, setSubmissionResult] = useState(null);

  const languages = [
    { value: 'python', label: 'Python' },
    { value: 'java', label: 'Java' },
    { value: 'cpp', label: 'C++' },
    { value: 'javascript', label: 'JavaScript' },
    { value: 'c', label: 'C' }
  ];

  const defaultCode = {
    python: `# Read input
n = int(input())
nums = list(map(int, input().split()))
target = int(input())

# Solution
for i in range(n):
    for j in range(i+1, n):
        if nums[i] + nums[j] == target:
            print(i, j)
            exit()`,
    java: `import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = sc.nextInt();
        }
        int target = sc.nextInt();
        
        // Solution
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] + nums[j] == target) {
                    System.out.println(i + " " + j);
                    return;
                }
            }
        }
    }
}`,
    cpp: `#include <iostream>
using namespace std;

int solution() {
    // Write your code here
    return 0;
}

int main() {
    int n;
    cin >> n;
    cout << solution() << endl;
    return 0;
}`,
    javascript: `function solution() {
    // Write your code here
    return 0;
}

// Read input
const readline = require('readline');
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

rl.on('line', (line) => {
    const n = parseInt(line);
    console.log(solution());
    rl.close();
});`,
    c: `#include <stdio.h>

int solution() {
    // Write your code here
    return 0;
}

int main() {
    int n;
    scanf("%d", &n);
    printf("%d\\n", solution());
    return 0;
}`
  };

  useEffect(() => {
    fetchProblem();
  }, [id]);

  useEffect(() => {
    setCode(defaultCode[language] || '');
  }, [language]);

  const fetchProblem = async () => {
    try {
      setLoading(true);
      const response = await api.get(`/problems/${id}`);
      setProblem(response.data);
    } catch (err) {
      setError('Failed to load problem');
      console.error('Problem error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async () => {
    if (!code.trim()) {
      alert('Please write some code before submitting');
      return;
    }

    try {
      setSubmitting(true);
      setSubmissionResult({
        status: 'PENDING',
        message: 'Submitting code and evaluating...'
      });
      
      const response = await api.post('/submissions/submit-and-evaluate', null, {
        params: {
          userId: user.id,
          problemId: id,
          code: code,
          language: language
        }
      });

      setSubmissionResult({
        status: response.data.status,
        message: getStatusMessage(response.data.status),
        accuracy: response.data.accuracy,
        timeTaken: response.data.timeTaken,
        testCasesPassed: response.data.testCasesPassed,
        totalTestCases: response.data.totalTestCases,
        analysisFeedback: response.data.analysisFeedback,
        efficiencyScore: response.data.efficiencyScore
      });

    } catch (err) {
      console.error('Submission error:', err);
      setSubmissionResult({
        status: 'ERROR',
        message: 'Failed to submit code. Please try again.'
      });
    } finally {
      setSubmitting(false);
    }
  };

  const checkSubmissionResult = async (submissionId) => {
    try {
      const response = await api.put(`/submissions/${submissionId}/result`);
      setSubmissionResult({
        status: response.data.status,
        message: getStatusMessage(response.data.status),
        accuracy: response.data.accuracy,
        timeTaken: response.data.timeTaken
      });
    } catch (err) {
      setSubmissionResult({
        status: 'ERROR',
        message: 'Failed to get submission result'
      });
    }
  };

  const getStatusMessage = (status) => {
    switch (status) {
      case 'ACCEPTED':
        return 'Congratulations! Your solution is correct!';
      case 'WRONG_ANSWER':
        return 'Your solution produced incorrect output.';
      case 'TIME_LIMIT_EXCEEDED':
        return 'Your solution took too long to execute.';
      case 'MEMORY_LIMIT_EXCEEDED':
        return 'Your solution used too much memory.';
      case 'RUNTIME_ERROR':
        return 'Your solution encountered a runtime error.';
      case 'COMPILATION_ERROR':
        return 'Your solution failed to compile.';
      default:
        return 'Checking your solution...';
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'ACCEPTED':
        return 'text-green-600 bg-green-100';
      case 'WRONG_ANSWER':
      case 'RUNTIME_ERROR':
      case 'COMPILATION_ERROR':
        return 'text-red-600 bg-red-100';
      case 'TIME_LIMIT_EXCEEDED':
      case 'MEMORY_LIMIT_EXCEEDED':
        return 'text-yellow-600 bg-yellow-100';
      default:
        return 'text-blue-600 bg-blue-100';
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (error || !problem) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="text-red-500 text-xl mb-4">{error || 'Problem not found'}</div>
          <button 
            onClick={() => navigate('/problems')}
            className="btn-primary"
          >
            Back to Problems
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto space-y-6">
      {/* Problem Header */}
      <div className="card">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">{problem.title}</h1>
            <div className="flex items-center space-x-4">
              <span className={`badge ${
                problem.difficulty === 'EASY' ? 'badge-easy' :
                problem.difficulty === 'MEDIUM' ? 'badge-medium' :
                'badge-hard'
              }`}>
                {problem.difficulty}
              </span>
              <span className="badge bg-blue-100 text-blue-800">
                {problem.topic.replace('_', ' ')}
              </span>
            </div>
          </div>
          <button
            onClick={() => navigate('/problems')}
            className="btn-secondary"
          >
            Back to Problems
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm text-gray-600">
          <div>Time Limit: {problem.timeLimit}ms</div>
          <div>Memory Limit: {problem.memoryLimit}MB</div>
          <div>Language: {language.toUpperCase()}</div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Problem Description */}
        <div className="space-y-6">
          <div className="card">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">Description</h2>
            <div className="prose max-w-none">
              <p className="text-gray-700 whitespace-pre-wrap">{problem.description}</p>
            </div>
          </div>

          {problem.inputFormat && (
            <div className="card">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Input Format</h3>
              <div className="bg-gray-50 p-4 rounded-lg">
                <pre className="text-sm text-gray-700 whitespace-pre-wrap">{problem.inputFormat}</pre>
              </div>
            </div>
          )}

          {problem.outputFormat && (
            <div className="card">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Output Format</h3>
              <div className="bg-gray-50 p-4 rounded-lg">
                <pre className="text-sm text-gray-700 whitespace-pre-wrap">{problem.outputFormat}</pre>
              </div>
            </div>
          )}

          {problem.constraints && (
            <div className="card">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Constraints</h3>
              <div className="bg-gray-50 p-4 rounded-lg">
                <pre className="text-sm text-gray-700 whitespace-pre-wrap">{problem.constraints}</pre>
              </div>
            </div>
          )}

          {problem.testCases && problem.testCases.length > 0 && (
            <div className="card">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Sample Test Cases</h3>
              {problem.testCases.filter(tc => tc.isSample).map((testCase, index) => (
                <div key={index} className="mb-4 last:mb-0">
                  <div className="bg-gray-50 p-4 rounded-lg">
                    <div className="mb-2">
                      <strong>Input:</strong>
                      <pre className="text-sm text-gray-700 mt-1 whitespace-pre-wrap">{testCase.inputData}</pre>
                    </div>
                    <div>
                      <strong>Expected Output:</strong>
                      <pre className="text-sm text-gray-700 mt-1 whitespace-pre-wrap">{testCase.expectedOutput}</pre>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Code Editor */}
        <div className="space-y-6">
          <div className="card">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-semibold text-gray-900">Code Editor</h2>
              <select
                value={language}
                onChange={(e) => setLanguage(e.target.value)}
                className="input w-auto"
              >
                {languages.map(lang => (
                  <option key={lang.value} value={lang.value}>{lang.label}</option>
                ))}
              </select>
            </div>
            
            <CodeEditor
              value={code}
              onChange={setCode}
              language={language}
              height="500px"
            />
          </div>

          {/* Submission Controls */}
          <div className="card">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold text-gray-900">Submit Solution</h3>
              <button
                onClick={handleSubmit}
                disabled={submitting || !code.trim()}
                className="btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {submitting ? 'Submitting...' : 'Submit Code'}
              </button>
            </div>

            {submissionResult && (
              <div className={`p-4 rounded-lg ${getStatusColor(submissionResult.status)}`}>
                <div className="font-medium">{submissionResult.message}</div>
                {submissionResult.accuracy !== undefined && (
                  <div className="text-sm mt-1">
                    Accuracy: {submissionResult.accuracy.toFixed(1)}%
                  </div>
                )}
                {submissionResult.efficiencyScore !== undefined && (
                  <div className="text-sm mt-1">
                    Efficiency Score: {submissionResult.efficiencyScore.toFixed(1)}/100
                  </div>
                )}
                {submissionResult.testCasesPassed !== undefined && submissionResult.totalTestCases !== undefined && (
                  <div className="text-sm mt-1">
                    Test Cases: {submissionResult.testCasesPassed}/{submissionResult.totalTestCases}
                  </div>
                )}
                {submissionResult.timeTaken && (
                  <div className="text-sm mt-1">
                    Time: {submissionResult.timeTaken}ms
                  </div>
                )}
                {submissionResult.analysisFeedback && (
                  <div className="mt-3 p-3 bg-gray-50 rounded-lg">
                    <div className="text-sm font-medium text-gray-700 mb-1">AI Analysis:</div>
                    <div className="text-sm text-gray-600">{submissionResult.analysisFeedback}</div>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProblemDetail;
