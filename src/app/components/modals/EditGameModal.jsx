'use client';

import { Button, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, Switch } from '@nextui-org/react';
import { useEffect, useState } from 'react';
import { IoMdHeart, IoMdHeartEmpty } from 'react-icons/io';

import { GET } from '@/app/api/signalRequest';
import { POST } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';
import AchivementsInput from '@/app/components/inputs/AchivementsInput';
import BacklogInput from '@/app/components/inputs/BacklogInput';
import ImageInput from '@/app/components/inputs/ImageInput';
import TagInput from '@/app/components/inputs/TagInput';

export default function EditGameModal({ game, setGames, isOpen, onOpenChange, session }) {
  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Form data
  const [imageKey, setImageKey] = useState(0);
  const [customImage, setCustomImage] = useState('');
  const [tags, setTags] = useState(game.tags);
  const [timePlayed, setTimePlayed] = useState(null);
  const [isFavorite, setIsFavorite] = useState(false);
  const [isFinished, setIsFinished] = useState(false);
  const [achievements, setAchievements] = useState(null);
  const [totalAchievements, setTotalAchievements] = useState(null);
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
    // Prevents from calling again if already fetched
    if (grids.length !== 0) {
      let index = grids.findIndex((grid) => grid === game.image);
      if (index !== -1) setImageKey(index);
      else setCustomImage(game.image);
    }

    // Fetch grids
    getGrids(game.sgdbId).then((filteredGrids) => {
      let index = filteredGrids.findIndex((grid) => grid === game.image);
      if (index !== -1) setImageKey(index);
      else setCustomImage(game.image);
    });
  }, [isOpen]);

  const renderModalContent = (onClose) => (
    <>
      <ModalHeader className="uppercase text-3xl">{game.name}</ModalHeader>
      <ModalBody className="grid grid-cols-2">
        <div className="space-y-4">
          {/* GAME IMAGE */}
          <ImageInput customImage={customImage} setCustomImage={setCustomImage} imageKey={imageKey} setImageKey={setImageKey} grids={grids} viewOnly={!editing} />
        </div>
        <div className="flex flex-col gap-4 w-full">
          {editing && (
            <>
              <div className="flex flex-row gap-4 justify-between">
                {/* FAVORITE */}
                <Switch size="lg" color="success" startContent={<IoMdHeart />} endContent={<IoMdHeartEmpty />} isSelected={isFavorite} onValueChange={setIsFavorite}>
                  Favorito
                </Switch>

                {/* FINISHED */}
                <Switch size="lg" color="success" isSelected={isFinished} onValueChange={setIsFinished}>
                  Terminado
                </Switch>
              </div>

              {/* BACKLOG */}
              <BacklogInput backlog={backlog} setBacklog={setBacklog} achievements={achievements} totalAchievements={totalAchievements} />

              {/* ACHIEVEMENTS */}
              <AchivementsInput achievements={achievements} setAchievements={setAchievements} totalAchievements={totalAchievements} setTotalAchievements={setTotalAchievements} />

              {/* TIME PLAYED */}
              <Input
                label="Tiempo jugado"
                placeholder="Introduce el tiempo jugado en minutos"
                type="number"
                variant="bordered"
                onChange={(e) => setTimePlayed(e.target.value)}
                value={timePlayed}
                endContent={'min'}
              />
            </>
          )}

          {/* TAGS */}
          <TagInput tags={tags} setTags={setTags} viewOnly={!editing} />
        </div>
      </ModalBody>
      <ModalFooter>
        {editing ? (
          <>
            <Button color="error" variant="flat" onClick={() => setEditing(false)}>
              Cancelar
            </Button>
            <Button color="success" variant="flat" onClick={() => saveGame(onClose)}>
              Añadir
            </Button>
          </>
        ) : (
          <Button color="success" variant="flat" onClick={() => setEditing(true)}>
            Editar y añadir
          </Button>
        )}
        <Button color="primary" variant="flat" onPress={onClose}>
          Close
        </Button>
      </ModalFooter>
    </>
  );
  return (
    <>
      <Modal isOpen={isOpen} size={'3xl'} onOpenChange={onOpenChange} placement="top-center">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
