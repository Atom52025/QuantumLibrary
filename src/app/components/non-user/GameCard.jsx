'use client';

import { useDisclosure } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import { useEffect } from 'react';

import Image from 'next/image';

import EditGameModal from '@/app/components/modals/EditGameModal';

export default function UserGameCard({ entry, setGames, random, setRandom, noModal = false }) {
  // Modal state
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  // Get Session
  const { data: session } = useSession();

  useEffect(() => {
    if (random === entry.id) {
      onOpen();
    }
  }, [random]);

  useEffect(() => {
    if (!isOpen && random === entry.id) {
      setRandom(-1);
    }
  }, [isOpen]);

  return (
    <>
      <button className=" group z-10 w-full h-full relative" onClick={noModal ? null : () => onOpen()}>
        <Image src={entry.image} alt={entry.name} fill sizes={'20vh'} />
        <div className="z-10 absolute bottom-0 w-full text-center font-bold uppercase bg-gray-900/40 p-1 opacity-0 group-hover:opacity-100">{entry.name}</div>
      </button>
      {session && <EditGameModal game={entry} setGames={setGames} isOpen={isOpen} onOpenChange={onOpenChange} session={session} />}
    </>
  );
}
