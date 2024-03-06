'use client';

import { Checkbox, CheckboxGroup } from '@nextui-org/react';
import React, { useEffect, useState } from 'react';
import { FaRandom } from 'react-icons/fa';

import GameCard from '@/app/components/GameCard';
import SortBy from '@/app/components/SortBy';
import FilterBar from '@/app/components/inputs/FilterBar';
import AddUserGameModal from '@/app/components/modals/AddUserGameModal';

export default function UserContentFiltering({ data }) {
  const [tags, setTags] = useState([]);
  const [selectedTags, setSelectedTags] = useState([]);
  const [games, setGames] = useState(data);
  const [filteredGames, setFilteredGames] = useState(games.sort((a, b) => a.name.localeCompare(b.name)));
  const [searchParam, setSearchParam] = useState('');
  const [random, setRandom] = useState(-1);

  const handleTagChange = (values) => {
    setSelectedTags(values);
  };

  const filterTags = () => {
    const tags = games
      .map(({ tags }) => tags.map((tag) => tag.trim()))
      .reduce((allTags, gameTags) => {
        gameTags.forEach((tag) => allTags.add(tag));
        return allTags;
      }, new Set());
    setTags([...tags].sort((a, b) => a.localeCompare(b)));
  };

  const orderBy = (order) => {
    switch (order) {
      case 'nameDown':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => a.name.localeCompare(b.name)));
        break;
      case 'nameUp':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => b.name.localeCompare(a.name)));
        break;
    }
  };

  const filterGames = () => {
    if (selectedTags.length === 0) {
      setFilteredGames(games.filter(({ name }) => name.toLowerCase().includes(searchParam.toLowerCase())));
    } else {
      setFilteredGames(
        games
          .filter(({ tags }) => selectedTags.every((tag) => tags.map((tag) => tag.trim()).includes(tag)))
          .filter(({ name }) => name.toLowerCase().includes(searchParam.toLowerCase()))
          .sort((a, b) => a.name.localeCompare(b.name)),
      );
    }
  };

  const randomGame = () => {
    const random = Math.floor(Math.random() * filteredGames.length);
    setRandom(filteredGames[random].id);
    console.log(random);
  };

  useEffect(() => {
    filterGames();
  }, [games, selectedTags, searchParam]);

  useEffect(() => {
    filterTags();
  }, [games]);

  return (
    <>
      <div className="h-full min-w-[200px] bg-gray-800/30 shadow-inner flex flex-col p-3">
        <CheckboxGroup label="Tags Filters" values={selectedTags} onChange={handleTagChange}>
          {tags?.map((tag) => (
            <Checkbox key={tag} value={tag}>
              {tag}
            </Checkbox>
          ))}
        </CheckboxGroup>
      </div>
      <main className="min-h-full w-full shadow-inner">
        <div className="flex flex-col ">
          <div className=" px-10 pt-5 flex justify-between">
            <FilterBar searchParam={searchParam} setSearchParam={setSearchParam} />
            <SortBy orderBy={orderBy} />
          </div>
          <div className="px-10 py-5 grid grid-cols-9 gap-3">
            {filteredGames?.map((entry) => (
              <div key={entry.id} className="aspect-[6/9] bg-gray-600 rounded-xl overflow-hidden">
                <GameCard entry={entry} setGames={setGames} random={random} />
              </div>
            ))}
          </div>
        </div>
      </main>
      <div className="fixed bottom-5 right-10 rounded-full aspect-square w-[100px] bg-gray-800 flex justify-center text-center items-center text-3xl z-20 group">
        <p className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 group-hover:opacity-0 opacity-100">{filteredGames.length}</p>
        <button className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 group-hover:opacity-100 opacity-0" onClick={() => randomGame()}>
          <FaRandom />
        </button>
      </div>
    </>
  );
}
