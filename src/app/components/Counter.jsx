import React from 'react';
import { FaRandom } from 'react-icons/fa';

export default function Counter({ open, filteredGames, randomGame }) {
  return (
    <div
      className={`fixed bottom-5 rounded-full aspect-square lg:w-[100px] w-[50px] bg-gray-800 flex justify-center text-center items-center lg:text-3xl text-lg z-20 group ${open ? 'right-[220px]' : 'right-[20px]'}`}>
      <p className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 group-hover:opacity-0 opacity-100">{filteredGames.length}</p>
      <button className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 group-hover:opacity-100 opacity-0" onClick={() => randomGame()}>
        <FaRandom />
      </button>
    </div>
  );
}
