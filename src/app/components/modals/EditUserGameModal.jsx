'use client';

import { Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, ScrollShadow } from '@nextui-org/react';
import { useEffect, useState } from 'react';

import { DELETE, GET, PATCH } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';
import GameInputs from '@/app/components/inputs/GameInputs';
import ImageInput from '@/app/components/inputs/ImageInput';

export default function EditUserGameModal({ userGame, setGames, isOpen, onOpenChange, session }) {
  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Form data
  const [timePlayed, setTimePlayed] = useState(userGame.timePlayed);
  const [imageKey, setImageKey] = useState(0);
  const [customImage, setCustomImage] = useState('');
  const [tags, setTags] = useState(userGame.tags);
  const [isFavorite, setIsFavorite] = useState(userGame.favorite);
  const [isFinished, setIsFinished] = useState(userGame.finished);
  const [achievements, setAchievements] = useState(userGame.achivements);
  const [totalAchievements, setTotalAchievements] = useState(userGame.totalAchivements);
  const [backlog, setBacklog] = useState(new Set([]));

  // Grid Images
  const [grids, setGrids] = useState([]);

  const eraseForm = async (onClose) => {
    const formURL = `api/user/${session.user.username}/games/${userGame.game.id}`;
    try {
      await DELETE(formURL, session.user.token);

      setGames((prevGames) => prevGames.filter((game) => game !== userGame));

      setResultModal('Juego eliminado con exito');
      onClose();
    } catch (error) {
      setResultModal('Error al eliminar el juego');
    }
  };

  const editForm = async (onClose) => {
    const formURL = `api/user/${session.user.username}/games/${userGame.game.id}`;

    const requestBody = {
      tags: tags,
      timePlayed: timePlayed,
      image: customImage || grids[imageKey],
      favorite: isFavorite,
      finished: isFinished,
      achivements: achievements,
      totalAchivements: totalAchievements,
      backlog: backlog.values().next().value,
    };

    try {
      const res = await PATCH(formURL, session.user.token, requestBody);

      setGames((prevGames) => prevGames.map((game) => (game.game.id === res.game.id ? res : game)));

      setResultModal('Juego editado con exito');
      onClose();
    } catch (error) {
      setResultModal('Error al editar el juego');
    }
  };

  const getGrids = async (key) => {
    const formURL = `api/sgdb/getGrids/${key}`;
    let res = await GET(formURL, session.user.token);
    let filteredGrids = res.data.filter((item) => item.width === 600 && item.height === 900).map((item) => item.url);
    setGrids(filteredGrids);
    return filteredGrids;
  };

  useEffect(() => {
    if (isOpen) {
      // Prevents from calling again if already fetched
      if (grids.length !== 0) {
        let index = grids.findIndex((grid) => grid === (userGame.image == null ? userGame.game.image : userGame.image));
        if (index !== -1) setImageKey(index);
        else setCustomImage(userGame.image == null ? userGame.game.image : userGame.image);
      }

      // Fetch grids
      getGrids(userGame.game.sgdbId).then((filteredGrids) => {
        let index = filteredGrids.findIndex((grid) => grid === (userGame.image == null ? userGame.game.image : userGame.image));
        if (index !== -1) setImageKey(index);
        else setCustomImage(userGame.image == null ? userGame.game.image : userGame.image);
      });
    }
  }, [isOpen]);

  useEffect(() => {
    if (totalAchievements === achievements) setBacklog(new Set([]));
  }, [achievements, totalAchievements]);

  const renderModalContent = (onClose) => (
    <>
      <ModalHeader className="uppercase sm:text-3xl text-xl">{userGame.game.name}</ModalHeader>
      <ModalBody className="overflow-auto">
        <ScrollShadow hideScrollBar className="flex flex-col sm:flex-row gap-5 relative">
          <div className="space-y-4 flex-shrink">
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
      <ModalFooter className="flex flex-wrap">
        <Button className="sm:w-auto w-full" color="danger" onPress={() => eraseForm(onClose)}>
          Eliminar
        </Button>
        <Button className="sm:w-auto w-full" color="warning" onPress={() => editForm(onClose)}>
          Guardar
        </Button>
        <Button className="sm:w-auto w-full" color="primary" variant="flat" onPress={onClose}>
          Cerrar
        </Button>
      </ModalFooter>
    </>
  );

  return (
    <>
      <Modal isOpen={isOpen} size={'5xl'} className=" max-h-[80vh] overflow-hidden" onOpenChange={onOpenChange} placement="top-center">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
