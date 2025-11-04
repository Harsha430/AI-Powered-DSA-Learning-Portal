import React from 'react';
import { Link } from 'react-router-dom';

function ProblemCard({ problem }) {
  const getDifficultyColor = (difficulty) => {
    switch (difficulty) {
      case 'EASY':
        return 'badge-easy';
      case 'MEDIUM':
        return 'badge-medium';
      case 'HARD':
        return 'badge-hard';
      default:
        return 'badge-easy';
    }
  };

  const getTopicColor = (topic) => {
    const colors = {
      'ARRAYS': 'bg-blue-100 text-blue-800',
      'STRINGS': 'bg-green-100 text-green-800',
      'TREES': 'bg-yellow-100 text-yellow-800',
      'GRAPHS': 'bg-purple-100 text-purple-800',
      'DYNAMIC_PROGRAMMING': 'bg-red-100 text-red-800',
      'GREEDY': 'bg-indigo-100 text-indigo-800',
      'SORTING': 'bg-pink-100 text-pink-800',
      'SEARCHING': 'bg-orange-100 text-orange-800',
      'MATH': 'bg-teal-100 text-teal-800',
      'HASH_TABLE': 'bg-cyan-100 text-cyan-800',
      'STACK': 'bg-amber-100 text-amber-800',
      'QUEUE': 'bg-lime-100 text-lime-800',
      'LINKED_LIST': 'bg-emerald-100 text-emerald-800',
      'BINARY_TREE': 'bg-violet-100 text-violet-800',
      'HEAP': 'bg-rose-100 text-rose-800',
    };
    return colors[topic] || 'bg-gray-100 text-gray-800';
  };

  return (
    <div className="card hover:shadow-lg transition-shadow duration-200">
      <div className="flex justify-between items-start mb-4">
        <h3 className="text-lg font-semibold text-gray-900 line-clamp-2">
          {problem.title}
        </h3>
        <span className={`badge ${getDifficultyColor(problem.difficulty)} ml-2 flex-shrink-0`}>
          {problem.difficulty}
        </span>
      </div>
      
      <p className="text-gray-600 text-sm mb-4 line-clamp-3">
        {problem.description}
      </p>
      
      <div className="flex flex-wrap gap-2 mb-4">
        <span className={`badge ${getTopicColor(problem.topic)}`}>
          {problem.topic.replace('_', ' ')}
        </span>
      </div>
      
      <div className="flex justify-between items-center text-sm text-gray-500 mb-4">
        <span>Time Limit: {problem.timeLimit}ms</span>
        <span>Memory Limit: {problem.memoryLimit}MB</span>
      </div>
      
      <Link 
        to={`/problems/${problem.id}`}
        className="btn-primary w-full text-center block"
      >
        Solve Problem
      </Link>
    </div>
  );
}

export default ProblemCard;
