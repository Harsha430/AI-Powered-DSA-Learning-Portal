import React from 'react';
import { 
  BarChart, 
  Bar, 
  XAxis, 
  YAxis, 
  CartesianGrid, 
  Tooltip, 
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell
} from 'recharts';

function DashboardCharts({ stats }) {
  const accuracyData = stats.accuracyByTopic ? 
    Object.entries(stats.accuracyByTopic).map(([topic, accuracy]) => ({
      topic: topic.replace('_', ' '),
      accuracy: Math.round(accuracy)
    })) : [];

  const pieData = [
    { name: 'Solved', value: stats.solvedProblems || 0, color: '#22c55e' },
    { name: 'Remaining', value: (stats.totalProblems || 0) - (stats.solvedProblems || 0), color: '#e5e7eb' }
  ];

  const COLORS = ['#22c55e', '#e5e7eb'];

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
      {/* Accuracy by Topic Chart */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">
          Accuracy by Topic
        </h3>
        <div className="h-64">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={accuracyData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis 
                dataKey="topic" 
                angle={-45}
                textAnchor="end"
                height={80}
                fontSize={12}
              />
              <YAxis 
                domain={[0, 100]}
                tickFormatter={(value) => `${value}%`}
              />
              <Tooltip 
                formatter={(value) => [`${value}%`, 'Accuracy']}
                labelFormatter={(label) => `Topic: ${label}`}
              />
              <Bar 
                dataKey="accuracy" 
                fill="#3b82f6"
                radius={[4, 4, 0, 0]}
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Problems Solved Chart */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">
          Problems Progress
        </h3>
        <div className="h-64">
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie
                data={pieData}
                cx="50%"
                cy="50%"
                innerRadius={60}
                outerRadius={100}
                paddingAngle={5}
                dataKey="value"
              >
                {pieData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip 
                formatter={(value, name) => [value, name]}
              />
            </PieChart>
          </ResponsiveContainer>
        </div>
        <div className="mt-4 text-center">
          <p className="text-2xl font-bold text-gray-900">
            {stats.solvedProblems || 0} / {stats.totalProblems || 0}
          </p>
          <p className="text-sm text-gray-600">Problems Solved</p>
        </div>
      </div>
    </div>
  );
}

export default DashboardCharts;
