'use client';

import { Button, Card, CardBody, Chip, Image, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, useDisclosure } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import { useEffect, useState } from 'react';

import { POST } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';
import ImageInput from '@/app/components/inputs/ImageInput';
import SearchBar from '@/app/components/inputs/SearchBar';
import TagInput from '@/app/components/inputs/TagInput';

export default function AddGameModal({ setGames }) {
  // Get Session
  const { data: session } = useSession();

  // Modal state
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Form data
  const [newGame, setNewGame] = useState(0);
  const [timePlayed, setTimePlayed] = useState(0);
  const [imageKey, setImageKey] = useState(0);
  const [customImage, setCustomImage] = useState('');
  const [tags, setTags] = useState([]);

  // Grid Images
  const [grids, setGrids] = useState([]);

  const passImage = (increment) => {
    setImageKey((prevKey) => (prevKey + increment + grids.length) % grids.length);
  };

  const submitForm = async (onClose) => {
    const formURL = `api/user/${session.user.username}/games/${newGame.key}`;

    const requestBody = {
      name: newGame.name,
      tags: tags.join(','),
      timePlayed: timePlayed,
      image: customImage || grids[imageKey],
    };

    try {
      let game = await POST(formURL, session.user.token, requestBody);
      setResultModal('success');
      setGames((prevGames) => [...prevGames, game]);
      onClose();
    } catch (error) {
      setResultModal('error');
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      setResultModal('closed');
    }, 5000);

    return () => clearTimeout(timer);
  }, [resultModal]);

  const renderModalContent = (onClose) => (
    <>
      <ModalHeader className="uppercase text-3xl">Add Game</ModalHeader>
      <ModalBody className="grid grid-cols-2 ">
        <div className="space-y-4">
          {/* GAME NAME */}
          <SearchBar setGame={setNewGame} setGrids={setGrids} />
          {/* GAME IMAGE */}
          <ImageInput customImage={customImage} setCustomImage={setCustomImage} imageKey={imageKey} setImageKey={setImageKey} grids={grids} passImage={passImage} />
        </div>
        <div className="flex flex-col gap-4 w-full">
          {/* TIME PLAYED */}
          <Input label="Time Played" placeholder="Enter time played" type="number" variant="bordered" onChange={(e) => setTimePlayed(e.target.value)} value={timePlayed} required />
          {/* GAME TAGS */}
          <TagInput tags={tags} setTags={setTags} />
        </div>
      </ModalBody>
      <ModalFooter>
        <Button color="danger" variant="flat" onPress={onClose}>
          Close
        </Button>
        <Button color="primary" onPress={() => submitForm(onClose)}>
          Add Game
        </Button>
      </ModalFooter>
    </>
  );

  return (
    <>
      <Button onPress={onOpen}>
        <div className="text-4xl font-bold">+</div>
        <div className="text-xl">Add Game</div>
      </Button>
      <Modal isOpen={isOpen} size={'3xl'} onOpenChange={onOpenChange} placement="top-center">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
