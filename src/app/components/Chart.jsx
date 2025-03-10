'use client';

import dynamic from 'next/dynamic';

import 'chart.js/auto';

const Doughnut = dynamic(() => import('react-chartjs-2').then((mod) => mod.Doughnut), {
  ssr: false,
});

export default function Chart({ obteinedData, fullData, labelObtenied, labelFull, games }) {
  const data = {
    labels: [labelFull, labelObtenied],
    datasets: [
      {
        data: [fullData - obteinedData, obteinedData],
        fill: false,
        backgroundColor: ['rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)'],
        borderColor: ['rgba(255, 99, 132, 1)', 'rgba(75, 192, 192, 1)'],
        borderWidth: 1,
        tension: 0.1,
      },
    ],
  };

  const options = {
    plugins: {
      legend: {
        display: false,
      },
    },
  };

  return (
    <div className="object-contain justify-center flex w-full">
      <div className="relative">
        <Doughnut data={data} options={options} />
        <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
          <p className="text-center text-2xl">
            Total: <br /> {games ? fullData : obteinedData}
          </p>
        </div>
      </div>
    </div>
  );
}
