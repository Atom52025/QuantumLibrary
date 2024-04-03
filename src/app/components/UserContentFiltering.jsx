'use client';

import { Button, Checkbox, CheckboxGroup } from '@nextui-org/react';
import React, { useEffect, useState } from 'react';
import { FaRandom } from 'react-icons/fa';

import SortBy from '@/app/components/SortBy';
import UserGameCard from '@/app/components/UserGameCard';
import FilterBar from '@/app/components/inputs/FilterBar';
import AddUserGameModal from '@/app/components/modals/AddUserGameModal';
import SteamImportModal from '@/app/components/modals/SteamImportModal';

export default function UserContentFiltering({ data }) {
  const [tags, setTags] = useState([]);
  const [selectedTags, setSelectedTags] = useState([]);
  const [games, setGames] = useState(data);
  const [filteredGames, setFilteredGames] = useState(games.sort((a, b) => a.game.name.localeCompare(b.game.name)));
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
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => a.game.name.localeCompare(b.game.name)));
        break;
      case 'nameUp':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => b.game.name.localeCompare(a.game.name)));
        break;
      case 'hoursDown':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => a.timePlayed - b.timePlayed));
        break;
      case 'hoursUp':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => b.timePlayed - a.timePlayed));
        break;
    }
  };

  const filterGames = () => {
    if (selectedTags.length === 0) {
      setFilteredGames(games.filter(({ game }) => game.name.toLowerCase().includes(searchParam.toLowerCase())));
    } else {
      setFilteredGames(
        games
          .filter(({ tags }) => selectedTags.every((tag) => tags.map((tag) => tag.trim()).includes(tag)))
          .filter(({ game }) => game.name.toLowerCase().includes(searchParam.toLowerCase()))
          .sort((a, b) => a.game.name.localeCompare(b.game.name)),
      );
    }
  };

  const randomGame = () => {
    const random = Math.floor(Math.random() * filteredGames.length);
    setRandom(filteredGames[random].game.id);
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
      <main className="max-h-full w-full shadow-inner overflow-y-scroll">
        <div className="flex flex-col ">
          <div className=" px-10 pt-5 flex justify-between">
            <FilterBar searchParam={searchParam} setSearchParam={setSearchParam} />
            <div className="flex flex-row gap-5">
              <SteamImportModal setGames={setGames} />
              <SortBy orderBy={orderBy} />
            </div>
          </div>
          <div className="px-10 py-5 grid grid-cols-6 gap-3">
            {filteredGames?.map((entry) => (
              <div key={entry.game.id} className="aspect-[6/9] bg-gray-600 rounded-xl overflow-hidden">
                <UserGameCard entry={entry} setGames={setGames} random={random} />
              </div>
            ))}
            <div className="aspect-[6/9] bg-gray-600 rounded-md overflow-hidden group border border-dashed border-gray-400 flex justify-center items-center">
              <AddUserGameModal setGames={setGames} />
            </div>
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
