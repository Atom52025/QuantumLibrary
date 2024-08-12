'use client';

import { Checkbox, CheckboxGroup, ScrollShadow } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';
import { FaRandom } from 'react-icons/fa';

import Counter from '@/app/components/Counter';
import GameCard from '@/app/components/GameCard';
import FilterBar from '@/app/components/inputs/FilterBar';
import SortByInput from '@/app/components/inputs/SortByInput';
import AddUserGameModal from '@/app/components/modals/AddUserGameModal';
import SteamImportModal from '@/app/components/modals/SteamImportModal';
import GroupListSection from '@/app/components/sections/GroupListSection';
import TagSection from '@/app/components/sections/TagSection';
import UserGameCard from '@/app/components/user/UserGameCard';

export default function ContentDisplay({ data, gData }) {
  const [selectedTags, setSelectedTags] = useState([]);
  const [games, setGames] = useState(data);
  const [filteredGames, setFilteredGames] = useState(games.sort((a, b) => a.name.localeCompare(b.name)));
  const [searchParam, setSearchParam] = useState('');
  const [random, setRandom] = useState(-1);
  const [open, setOpen] = useState(false);

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

  return (
    <>
      <TagSection games={games} selectedTags={selectedTags} setSelectedTags={setSelectedTags} />
      <ScrollShadow hideScrollBar className={`max-h-full w-full shadow-inner overflow-y-scroll ${open ? 'mr-[200px]' : 'mr-0'}`}>
        <div className="flex flex-col ">
          <div className=" px-10 pt-5 flex justify-between">
            <FilterBar searchParam={searchParam} setSearchParam={setSearchParam} />
            <SortByInput orderBy={orderBy} />
          </div>
          <div className="px-10 py-5 grid grid-cols-9 gap-3">
            {filteredGames?.map((entry) => (
              <div key={entry.id} className="aspect-[6/9] bg-gray-600 rounded-xl overflow-hidden">
                <GameCard entry={entry} setGames={setGames} random={random} />
              </div>
            ))}
          </div>
        </div>
      </ScrollShadow>
      {gData && <GroupListSection gData={gData} open={open} setOpen={setOpen} />}
      <Counter randomGame={randomGame} filteredGames={filteredGames} open={open} />
    </>
  );
}
