'use client';

import { ScrollShadow } from '@nextui-org/react';
import React, { useEffect, useState } from 'react';

import Counter from '@/app/components/Counter';
import FilterBar from '@/app/components/inputs/FilterBar';
import SortByInput from '@/app/components/inputs/SortByInput';
import AddUserGameModal from '@/app/components/modals/AddUserGameModal';
import SteamImportModal from '@/app/components/modals/SteamImportModal';
import GroupListSection from '@/app/components/sections/GroupListSection';
import TagSection from '@/app/components/sections/TagSection';
import UserGameCard from '@/app/components/user/UserGameCard';

export default function UserContentDisplay({ data, gData }) {
  const [selectedTags, setSelectedTags] = useState([]);
  const [games, setGames] = useState(data);
  const [filteredGames, setFilteredGames] = useState(games.sort((a, b) => a.game.name.localeCompare(b.game.name)));
  const [searchParam, setSearchParam] = useState('');
  const [random, setRandom] = useState(-1);
  const [open, setOpen] = useState(false);

  const orderBy = (order) => {
    switch (order) {
      case 'nameDown':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => a.game.name.localeCompare(b.game.name)));
        break;
      case 'nameUp':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => b.game.name.localeCompare(a.game.name)));
        break;
      case 'hoursDown':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => b.timePlayed - a.timePlayed));
        break;
      case 'hoursUp':
        setFilteredGames((prevGames) => [...prevGames].sort((a, b) => a.timePlayed - b.timePlayed));
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
  };

  useEffect(() => {
    filterGames();
  }, [games, selectedTags, searchParam]);

  return (
    <>
      <TagSection games={games} setSelectedTags={setSelectedTags} selectedTags={selectedTags} />
      <ScrollShadow hideScrollBar className={`max-h-full w-full shadow-inner overflow-y-scroll ${open ? 'mr-[200px]' : 'mr-0'}`}>
        <div className="flex flex-col ">
          <div className=" px-10 pt-5 flex justify-between">
            <FilterBar searchParam={searchParam} setSearchParam={setSearchParam} />
            <div className="flex flex-row gap-5">
              <SteamImportModal setGames={setGames} />
              <SortByInput orderBy={orderBy} usergame={true}/>
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
      </ScrollShadow>
      <GroupListSection gData={gData} open={open} setOpen={setOpen} />
      <Counter randomGame={randomGame} filteredGames={filteredGames} open={open} />
    </>
  );
}
