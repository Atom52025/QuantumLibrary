'use client';

import {Checkbox, CheckboxGroup, useDisclosure} from '@nextui-org/react';
import React, {useEffect, useState} from 'react';
import EditGameModal from '@/app/components/modals/EditGameModal';


import Image from 'next/image';

import SearchBar from '@/app/components/SearchBar';
import AddGameModal from '@/app/components/modals/AddGameModal';
import GameCard from "@/app/components/GameCard";

export default function ContentFiltering({ data }) {

  const [tags, setTags] = useState([]);
  const [selectedTags, setSelectedTags] = useState([]);
  const [games, setGames] = useState(data?.games);
  const [filteredGames, setFilteredGames] = useState(games);

  const handleTagChange = (values) => {
    setSelectedTags(values);
  };
  
  const filterTags = () => {
      const tags =
        games.map(({ tags }) => tags.split(',').map((tag) => tag.trim()))
        .reduce((allTags, gameTags) => {
            gameTags.forEach((tag) => allTags.add(tag));
            return allTags;
        }, new Set());
      setTags([...tags]);
  }
  
  const filterGames = () => {
      if (selectedTags.length === 0) {
          setFilteredGames(games);
      } else {
          setFilteredGames(
            games.filter(({tags}) =>
              selectedTags.some((tag) =>
                tags
                  .split(',')
                  .map((tag) => tag.trim())
                  .includes(tag),
              ),
            ),
          );
      }
  }
  
  useEffect(() => {
    filterGames();
  }, [games, selectedTags]);
  
  useEffect(() => {
      filterTags();
  }, [games]);
  
  useEffect(() => {
      filterTags();
  }, []);

  
  return (
    <>
      <div className="h-full min-w-[200px] bg-gray-800/30 shadow-inner flex flex-col p-1">
        <CheckboxGroup label="Tags Filters" values={selectedTags} onChange={handleTagChange}>
          {tags?.map((tag) => (
            <Checkbox key={tag} value={tag}>
              {tag}
            </Checkbox>
          ))}
        </CheckboxGroup>

      </div>
      <main className="min-h-full w-full ">
        <div className="p-10 grid grid-cols-6 gap-3">
          {filteredGames?.map((entry) => (
              <div key={entry.game.id} className="aspect-[6/9] bg-gray-600 rounded-md relative overflow-hidden">
                <GameCard entry={entry} setGames={setGames}/>
              </div>
          ))}
          <div className="aspect-[6/9] bg-gray-600 rounded-md overflow-hidden group border border-dashed border-gray-400 flex justify-center items-center">
            <AddGameModal setGames={setGames}/>
          </div>

        </div>
      </main>
    </>
  );
}
