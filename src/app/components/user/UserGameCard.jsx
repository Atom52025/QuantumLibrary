'use client';

import { useDisclosure } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import { useEffect } from 'react';
import { FaTrophy } from 'react-icons/fa';

import Image from 'next/image';

import EditUserGameModal from '@/app/components/modals/EditUserGameModal';

export default function UserGameCard({ entry, setGames, random }) {
  // Modal state
  const { isOpen, onOpen, onOpenChange } = useDisclosure({ defaultOpen: false });
  // Get Session
  const { data: session } = useSession({ required: true });

  if (entry.image === null && entry.game.image === null) {
    entry.image = 'https://placehold.jp/600x900.png';
  }

  useEffect(() => {
    if (random === entry.game.id) {
      onOpen();
    }
  }, [random]);

  return (
    <>
      <button className="group w-full h-full relative" onClick={() => onOpen()}>
        <Image src={entry.image ? entry.image : entry.game.image} alt={entry.game.name} fill sizes={'20vh'} />
        <div className="absolute bottom-0 w-full text-center font-bold uppercase text-sm lg:text-xl bg-gray-900/40 p-1 opacity-0 group-hover:opacity-100 text-white">{entry.game.name}</div>
        {entry.achivements === entry.totalAchivements && entry.totalAchivements > 0 && (
          <div className="z-10 absolute top-1 right-1 p-2 opacity-0 group-hover:opacity-100 bg-gray-800/50 rounded-full">
            <FaTrophy className="lg:text-3xl text-xl text-yellow-400 " />
          </div>
        )}
      </button>
      {session && <EditUserGameModal userGame={entry} setGames={setGames} isOpen={isOpen} onOpenChange={onOpenChange} session={session} />}
    </>
  );
}
