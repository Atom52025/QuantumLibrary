'use client';

import { Checkbox, CheckboxGroup } from '@nextui-org/react';
import { signIn, signOut, useSession } from 'next-auth/react';
import { useState } from 'react';

import Image from 'next/image';

import { Button } from '@nextui-org/button';
import { getToken } from 'next-auth/jwt';

export default function ContentFiltering({ data, tags }) {
  const [selectedTags, setSelectedTags] = useState([]);

  const handleTagChange = (values) => {
    setSelectedTags(values);
  };

  const filteredGames = data?.games.filter(({ game }) => {
    if (selectedTags.length === 0) {
      return true;
    }
    return selectedTags.some((tag) =>
      game.tags
        .split(',')
        .map((tag) => tag.trim())
        .includes(tag),
    );
  });

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
          {filteredGames?.map(({ game, timePlayed }) => (
            <div key={game.id} className="aspect-[6/9] bg-gray-600 rounded-md relative overflow-hidden group">
              <div className="z-10 w-full h-full relative">
                <Image src={game.image} alt={game.image} fill priority={true}></Image>
              </div>
              <div className="z-20 absolute bottom-0 w-full text-center font-bold uppercase bg-gray-900/40 p-1 opacity-0 group-hover:opacity-100">{game.name}</div>
            </div>
          ))}
          <div className="aspect-[6/9] bg-gray-600 rounded-md relative overflow-hidden group border border-dashed border-gray-400 flex justify-center items-center">
            <Button>
              <div className="text-4xl font-bold">+</div>
              <div className="text-xl">Add Game</div>
            </Button>
          </div>
        </div>
      </main>
    </>
  );
}
