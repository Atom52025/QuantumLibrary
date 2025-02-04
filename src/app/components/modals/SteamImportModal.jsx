import { Button, Card, CardHeader, Checkbox, CheckboxGroup, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, Spinner, User, useDisclosure } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';
import { PiArrowBendDownRightBold } from 'react-icons/pi';

import { GET, PATCH, POST } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';

export default function SteamImportModal({ setGames }) {
  // Get Session
  const { data: session, update } = useSession({ required: true });

  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Modal state
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  const [userId, setUserId] = useState('');
  const [user, setUser] = useState(null);
  const [foundGames, setFoundGames] = useState([]);
  const [groupSelected, setGroupSelected] = React.useState([]);
  const [searchLoading, setSearchLoading] = useState(false);
  const [importLoading, setImportLoading] = useState(false);

  useEffect(() => {
    if (userId) {
      const match = userId.match(/\/profiles\/(\d+)\//);
      if (match) {
        const extractedUserId = match[1];
        setUserId(extractedUserId);
      }
    }
  }, [userId]);

  const searchUser = async () => {
    const formURL = `api/steam/user/${userId}`;
    try {
      const res = await GET(formURL, session.user.token);
      setUser(res.response.players[0]);
    } catch (error) {
      setResultModal('Error al buscar el usuario');
    }
  };

  const searchGames = async () => {
    const formURL = `api/steam/games/${userId}`;
    try {
      setSearchLoading(true);

      const res = await GET(formURL, session.user.token);
      setFoundGames(res.games);

      setSearchLoading(false);
    } catch (error) {
      setResultModal('Error al buscar juegos');
    }
  };

  const importImg = async (image) => {
    const formURL = `api/users/${session.user.username}`;

    const requestBody = {
      image: image,
    };

    try {
      await PATCH(formURL, session.user.token, requestBody);
      setResultModal('Imagen de usuario importada con exito');
      await update({ ...session, user: { ...session?.user, image: image } });
    } catch (error) {
      setResultModal('Error al importar la imagen del usuario');
    }
  };

  const importGames = async (onClose) => {
    if (groupSelected.length === 0) {
      setResultModal('Aviso - Ningun juego seleccionado');
      return;
    }

    const formURL = `api/user/${session.user.username}/games/import`;

    const requestBody = {
      games: groupSelected,
    };

    try {
      setImportLoading(true);

      const res = await POST(formURL, session.user.token, requestBody);
      setGames((prevGames) => [...prevGames, ...res.games]);

      setImportLoading(false);
      setResultModal('Juegos importados con exito');
      onClose();
    } catch (error) {
      setResultModal('Error al importar los juegos');
    }
  };

  useEffect(() => {
    setGroupSelected(foundGames);
  }, [foundGames]);

  const renderModalContent = (onClose) => (
    <>
      <ModalHeader className="uppercase text-3xl">Import games from steam</ModalHeader>
      <ModalBody className="">
        <Input
          label="Steam User Id"
          placeholder="Introduzca su Steam User Id"
          type="text"
          variant="bordered"
          value={userId}
          onValueChange={setUserId}
          endContent={
            <Button isIconOnly onClick={searchUser} className="h-full">
              <PiArrowBendDownRightBold />
            </Button>
          }
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              searchUser();
            }
          }}
        />
        {user && (
          <div className="flex flex-row justify-between gap-10 ">
            <User
              name={user.realname}
              description={'@' + user.personaname}
              avatarProps={{
                src: user.avatarfull,
              }}
            />
            <div className="flex flex-row gap-3">
              <Button color="primary" onPress={() => importImg(user.avatarfull)}>
                Importar imagen
              </Button>
              <Button color="warning" onPress={() => searchGames()}>
                {searchLoading ? <Spinner /> : 'Buscar juegos'}
              </Button>
              <Button color="danger" variant="bordered" onClick={() => setUser(null)}>
                Eliminar usuario
              </Button>
            </div>
          </div>
        )}
        {foundGames.length !== 0 && (
          <CheckboxGroup
            label={'Selecciona los juegos que deseas importar: ' + groupSelected.length}
            value={groupSelected}
            onChange={setGroupSelected}
            classNames={{
              base: 'w-full',
            }}>
            {foundGames.map((game) => (
              <Checkbox
                aria-label={game.name}
                classNames={{
                  base: 'inline-flex !w-full max-w-full bg-content1 m-0',
                  label: 'w-full',
                }}
                value={game}
                key={game.name}>
                <Card className="bg-gray-600/40 w-full">
                  <CardHeader className="p-2 px-4 flex-row justify-between grid grid-cols-10">
                    <h4 className="font-bold text-large col-span-7">{game.name}</h4>
                    <p className="text-tiny uppercase font-bold col-span-2">Time played:</p>
                    <small className="text-default-500">{game.timePlayed} min</small>
                  </CardHeader>
                </Card>
              </Checkbox>
            ))}
          </CheckboxGroup>
        )}
      </ModalBody>
      <ModalFooter>
        <Button color="danger" onPress={onClose}>
          Cancelar
        </Button>
        <Button color="primary" onPress={() => importGames(onClose)} isDisabled={foundGames.length < 1}>
          {importLoading ? <Spinner color="default" /> : 'Importar'}
        </Button>
      </ModalFooter>
    </>
  );

  return (
    <>
      <Button onClick={onOpen} className="lg:flex-grow-0 flex-grow ">
        Importar desde STEAM
      </Button>
      <Modal isOpen={isOpen} size={'3xl'} onOpenChange={onOpenChange} placement="top-center" scrollBehavior="inside">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
