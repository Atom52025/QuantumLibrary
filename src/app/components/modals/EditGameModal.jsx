'use client';

import { Button, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, ScrollShadow, Switch } from '@nextui-org/react';
import { useEffect, useState } from 'react';
import { IoMdHeart, IoMdHeartEmpty } from 'react-icons/io';

import { GET, POST } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';
import AchivementsInput from '@/app/components/inputs/AchivementsInput';
import BacklogInput from '@/app/components/inputs/BacklogInput';
import GameInputs from '@/app/components/inputs/GameInputs';
import ImageInput from '@/app/components/inputs/ImageInput';
import TagInput from '@/app/components/inputs/TagInput';

export default function EditGameModal({ game, setGames, isOpen, onOpenChange, session }) {
  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Form data
  const [timePlayed, setTimePlayed] = useState(0);
  const [imageKey, setImageKey] = useState(0);
  const [customImage, setCustomImage] = useState('');
  const [tags, setTags] = useState(game.tags);
  const [isFavorite, setIsFavorite] = useState(false);
  const [isFinished, setIsFinished] = useState(false);
  const [achievements, setAchievements] = useState(0);
  const [totalAchievements, setTotalAchievements] = useState(0);
  const [backlog, setBacklog] = useState(new Set([]));
  const [editing, setEditing] = useState(false);

  // Grid Images
  const [grids, setGrids] = useState([]);

  const saveGame = async (onClose) => {
    const formURL = `api/user/${session.user.username}/games/${game.sgdbId}`;

    const requestBody = {
      name: game.name,
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
      const res = await POST(formURL, session.user.token, requestBody);

      setResultModal('Juego añadido a su biblioteca correctamente');
      onClose();
    } catch (error) {
      setResultModal('Error al añadir el juego, por favor, intentelo de nuevo');
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
        let index = grids.findIndex((grid) => grid === game.image);
        if (index !== -1) setImageKey(index);
        else setCustomImage(game.image);
      } else {
        // Fetch grids
        getGrids(game.sgdbId).then((filteredGrids) => {
          let index = filteredGrids.findIndex((grid) => grid === game.image);
          if (index !== -1) setImageKey(index);
          else setCustomImage(game.image);
        });
      }
    }
  }, [isOpen]);

  const renderModalContent = (onClose) => (
    <>
      <ModalHeader className="uppercase sm:text-3xl text-xl">{game.name}</ModalHeader>
      <ModalBody className="overflow-auto">
        <ScrollShadow hideScrollBar className="flex flex-col sm:flex-row gap-5">
          <div className="space-y-4 flex-shrink">
            {/* GAME IMAGE */}
            <ImageInput customImage={customImage} setCustomImage={setCustomImage} imageKey={imageKey} setImageKey={setImageKey} grids={grids} viewOnly={!editing} />
          </div>
          <div className="flex flex-col gap-4 w-full flex-grow">
            {editing ? (
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
            ) : (
              <TagInput tags={tags} setTags={setTags} viewOnly={!editing} />
            )}
          </div>
        </ScrollShadow>
      </ModalBody>
      <ModalFooter className="flex flex-wrap">
        {editing ? (
          <>
            <Button className="sm:w-auto w-full" color="error" variant="flat" onClick={() => setEditing(false)}>
              Cancelar
            </Button>
            <Button className="sm:w-auto w-full" color="success" variant="flat" onClick={() => saveGame(onClose)}>
              Añadir
            </Button>
          </>
        ) : (
          <Button className="sm:w-auto w-full" color="success" variant="flat" onClick={() => setEditing(true)}>
            Editar y añadir
          </Button>
        )}
        <Button className="sm:w-auto w-full" color="primary" variant="flat" onPress={onClose}>
          Cerrar
        </Button>
      </ModalFooter>
    </>
  );
  return (
    <>
      <Modal isOpen={isOpen} size={'4xl'} className="max-h-[80vh] overflow-hidden" onOpenChange={onOpenChange} placement="top-center">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
