'use client';

import { ScrollShadow } from '@nextui-org/react';
import React, { useEffect, useState } from 'react';

import Counter from '@/app/components/Counter';
import FilterBar from '@/app/components/inputs/FilterBar';
import SortByInput from '@/app/components/inputs/SortByInput';
import GameCard from '@/app/components/non-user/GameCard';
import GroupListSection from '@/app/components/sections/GroupListSection';
import TagSection from '@/app/components/sections/TagSection';

export default function GroupContentDisplay({ data, gData }) {
  const [games, setGames] = useState(data);
  const [filteredGames, setFilteredGames] = useState(games.sort((a, b) => a.name.localeCompare(b.name)));
  const [searchParam, setSearchParam] = useState('');
  const [random, setRandom] = useState(-1);
  const [open, setOpen] = useState(false);
  const [selectedTags, setSelectedTags] = useState([]);
  const [order, setOrder] = useState('nameDown');

  // Function to order the games
  useEffect(() => {
    switch (order) {
      case 'nameDown':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => a.name.localeCompare(b.name)));
        break;
      case 'nameUp':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => b.name.localeCompare(a.name)));
        break;
      case 'mostVoted': // New case for ordering by most votes
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => b.votes.length - a.votes.length));
        break;
      case 'leastVoted': // Option for least voted to most voted
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => a.votes.length - b.votes.length));
        break;
    }
  }, [order]);

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
  };

  useEffect(() => {
    filterGames();
  }, [games, selectedTags, searchParam]);

  return (
    <>
      <TagSection games={games} selectedTags={selectedTags} setSelectedTags={setSelectedTags} />
      <div className={`max-h-full w-full ${open ? 'mr-[200px]' : 'mr-0'}`}>
        <div className="w-full h-full flex flex-col relative">
          <div className="md:px-10 px-5 py-5 flex flex-wrap gap-2 md:justify-between justify-center sticky top-0 bg-gray-900">
            <FilterBar searchParam={searchParam} setSearchParam={setSearchParam} />
            <SortByInput setOrder={setOrder} />
          </div>
          <ScrollShadow className="h-full w-full shadow-inner overflow-y-scroll">
            <div className="lg:px-10 px-5 py-5 grid xl:grid-cols-9 lg:grid-cols-7 sm:grid-cols-5 grid-cols-3 gap-3">
              {filteredGames?.map((entry) => (
                <div key={entry.id} className="aspect-[6/9] bg-gray-600 rounded-xl overflow-hidden">
                  <GameCard entry={entry} setGames={setGames} random={random} />
                </div>
              ))}
            </div>
          </ScrollShadow>
        </div>
      </div>
      {gData && <GroupListSection gData={gData} open={open} setOpen={setOpen} />}
      <Counter randomGame={randomGame} filteredGames={filteredGames} open={open} />
    </>
  );
}
