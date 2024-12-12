'use client';

import { Button, Card, CardBody, Chip, Image, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, ScrollShadow, Switch, useDisclosure } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';
import { IoMdHeart, IoMdHeartEmpty } from 'react-icons/io';

import { POST } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';
import AchivementsInput from '@/app/components/inputs/AchivementsInput';
import BacklogInput from '@/app/components/inputs/BacklogInput';
import GameInputs from '@/app/components/inputs/GameInputs';
import ImageInput from '@/app/components/inputs/ImageInput';
import SearchBar from '@/app/components/inputs/SearchBar';
import TagInput from '@/app/components/inputs/TagInput';

export default function AddUserGameModal({ setGames }) {
  // Get Session
  const { data: session } = useSession();

  // Modal state
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Form data
  const [newGame, setNewGame] = useState('');
  const [timePlayed, setTimePlayed] = useState(0);
  const [imageKey, setImageKey] = useState(0);
  const [customImage, setCustomImage] = useState('');
  const [tags, setTags] = useState([]);
  const [isFavorite, setIsFavorite] = useState(false);
  const [isFinished, setIsFinished] = useState(false);
  const [achievements, setAchievements] = useState(0);
  const [totalAchievements, setTotalAchievements] = useState(0);
  const [backlog, setBacklog] = useState(new Set([]));

  // Grid Images
  const [grids, setGrids] = useState([]);

  const passImage = (increment) => {
    setImageKey((prevKey) => (prevKey + increment + grids.length) % grids.length);
  };

  const handleClose = (onClose) => {
    setGrids([]);
    setNewGame('');
    onClose();
  };

  const submitForm = async (onClose) => {
    if (!newGame) {
      setResultModal('Error: Introduce un juego');
      return;
    }

    const formURL = `api/user/${session.user.username}/games/${newGame.key}`;

    const requestBody = {
      name: newGame.name,
      tags: tags,
      timePlayed: timePlayed,
      image: customImage || grids[imageKey],
      defaultImage: grids[0],
      favorite: isFavorite,
      finished: isFinished,
      achivements: achievements,
      totalAchivements: totalAchievements,
      backlog: backlog.values().next().value,
    };

    try {
      let game = await POST(formURL, session.user.token, requestBody);
      setResultModal('Juego añadido correctamente');
      setGames((prevGames) => [...prevGames, game]);
      handleClose(onClose);
    } catch (error) {
      setResultModal('Error al añadir el juego');
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
      <ModalHeader className="uppercase sm:text-3xl text-xl">Añadir juego</ModalHeader>
      <ModalBody className="overflow-auto">
        <ScrollShadow hideScrollBar className="flex flex-col sm:flex-row gap-5 relative">
          <div className="space-y-4 flex-shrink">
            {/* GAME NAME */}
            <SearchBar setGame={setNewGame} setGrids={setGrids} />
            {/* GAME IMAGE */}
            <ImageInput customImage={customImage} setCustomImage={setCustomImage} imageKey={imageKey} setImageKey={setImageKey} grids={grids} />
          </div>
          <div className="flex flex-col gap-4 w-full flex-grow">
            <GameInputs
              isFavorite={isFavorite}
              setIsFavorite={setIsFavorite}
              isFinished={isFinished}
              setIsFinished={setIsFinished}
              backlog={backlog}
              setBacklog={setBacklog}
              achievements={achievements}
              setAchievements={setAchievements}
              totalAchievements={totalAchievements}
              setTotalAchievements={setTotalAchievements}
              timePlayed={timePlayed}
              setTimePlayed={setTimePlayed}
              tags={tags}
              setTags={setTags}
            />
          </div>
        </ScrollShadow>
      </ModalBody>
      <ModalFooter>
        <Button color="danger" variant="flat" onPress={() => handleClose(onClose)}>
          Cancelar
        </Button>
        <Button color="primary" onPress={() => submitForm(onClose)}>
          Añadir
        </Button>
      </ModalFooter>
    </>
  );
  return (
    <>
      <Button onPress={onOpen} className="lg:flex-grow-0 flex-grow ">
        <div className="font-bold">+</div>
        <div>Añadir Juego</div>
      </Button>
      <Modal isOpen={isOpen} size={'5xl'} className=" max-h-[80vh] overflow-hidden" onOpenChange={onOpenChange} placement="top-center">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
