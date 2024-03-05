import { Button, Card, CardBody, CardHeader, Checkbox, CheckboxGroup, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, Spinner, User, cn, useDisclosure } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useState } from 'react';
import { PiArrowBendDownRightBold } from 'react-icons/pi';

import Image from 'next/image';

import { GET } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';

export default function SteamImportModal() {
  // Get Session
  const { data: session } = useSession({ required: true });

  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Modal state
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  const [userId, setUserId] = useState('');
  const [user, setUser] = useState(null);
  const [games, setGames] = useState([]);
  const [groupSelected, setGroupSelected] = React.useState([]);
  const [loading, setLoading] = useState(false);

  const searchUser = async () => {
    const formURL = `api/steam/user/${userId}`;
    try {
      const res = await GET(formURL, session.user.token);
      console.log(res.response.players[0].personaname);
      setUser(res.response.players[0]);
    } catch (error) {
      setResultModal('error');
    }
  };

  const searchGames = async () => {
    const formURL = `api/steam/games/${userId}`;
    try {
      setLoading(true);
      const res = await GET(formURL, session.user.token);
      console.log(res.games);
      setGames(res.games);
      setLoading(false);
    } catch (error) {
      setResultModal('error');
    }
  };

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
            <Button color="warning" onPress={() => searchGames()}>
              {loading ? <Spinner /> : 'Search games'}
            </Button>
          </div>
        )}
        {games.length !== 0 && (
          <CheckboxGroup
            label="Select games to import"
            value={groupSelected}
            onChange={setGroupSelected}
            classNames={{
              base: 'w-full',
            }}>
            {games.map((game) => (
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
          Cancel
        </Button>
        <Button color="primary" onPress={onClose}>
          Import
        </Button>
      </ModalFooter>
    </>
  );

  return (
    <>
      <Button onClick={onOpen}> Import from STEAM </Button>
      <Modal isOpen={isOpen} size={'3xl'} onOpenChange={onOpenChange} placement="top-center" scrollBehavior="inside">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
