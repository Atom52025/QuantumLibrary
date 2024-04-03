'use client';

import { Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import { useEffect, useState } from 'react';

import { DELETE, PATCH } from '@/app/api/request';
import { GET } from '@/app/api/signalRequest';
import InfoPopups from '@/app/components/InfoPopups';
import ImageInput from '@/app/components/inputs/ImageInput';
import TagInput from '@/app/components/inputs/TagInput';

export default function EditGameModal({ game, setGames, isOpen, onOpenChange, session }) {
  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Form data
  const [imageKey, setImageKey] = useState(0);
  const [customImage, setCustomImage] = useState('');
  const [tags, setTags] = useState(game.tags);

  // Grid Images
  const [grids, setGrids] = useState([]);

  // Admin
  const [isAdmin, setIsAdmin] = useState(session.user.role === 'ADMIN');

  const eraseForm = async (onClose) => {
    const formURL = `api/games/${game.id}`;
    console.log(formURL);

    try {
      setGames((prevGames) => prevGames.filter((game) => game !== userGame));
      await DELETE(formURL);
      setResultModal('Game erased successfully');

      onClose();
    } catch (error) {
      setResultModal('Error erasing game');
    }
  };

  const editForm = async (onClose) => {
    const formURL = `api/games/${game.id}`;

    const requestBody = {
      tags: tags.join(','),
      image: customImage || grids[imageKey],
    };

    try {
      const res = await PATCH(formURL, requestBody);

      console.log(res);

      setResultModal('Game edited successfully');
      onClose();
    } catch (error) {
      setResultModal('Error editing game');
    }
  };

  const getGrids = async (key) => {
    console.log(game.name + ' - Session: ' + session);
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
          <ImageInput customImage={customImage} setCustomImage={setCustomImage} imageKey={imageKey} setImageKey={setImageKey} grids={grids} viewOnly={isAdmin} />
        </div>
        <div className="flex flex-col gap-4 w-full">
          {/* TAGS */}
          <TagInput tags={tags} setTags={setTags} />
        </div>
      </ModalBody>
      <ModalFooter>
        {isAdmin && (
          <>
            <Button color="danger" onPress={() => eraseForm(onClose)}>
              Erase
            </Button>
            <Button color="warning" onPress={() => editForm(onClose)}>
              Edit
            </Button>
          </>
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
