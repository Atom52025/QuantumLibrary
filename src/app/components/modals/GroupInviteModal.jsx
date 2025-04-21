import { Button, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, Spinner, User, useDisclosure } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useState } from 'react';
import { PiArrowBendDownRightBold } from 'react-icons/pi';

import { GET, POST } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';

export default function GroupInviteModal({ groupId, setGroups }) {
  // Get Session
  const { data: session } = useSession({ required: true });

  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Modal state
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  // Search state
  const [userId, setUserId] = useState('');
  const [foundUsers, setFoundUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  // Search user
  const searchUser = async () => {
    const formURL = `api/user/${userId}`;
    try {
      setLoading(true);

      const res = await GET(formURL, session.user.token);

      if (res === null) {
        setResultModal('Error: No se pudo encontrar al usuario');
      } else {
        setFoundUsers((prevFoundUsers) => {
          const exists = prevFoundUsers.some((user) => user.username === res.username);
          return exists ? prevFoundUsers : [...prevFoundUsers, res];
        });
      }

      setLoading(false);
    } catch (error) {
      setResultModal('Error al buscar el usuario');
    }
  };

  // Invite user to group
  const invite = async (onClose) => {
    const formURL = `api/group/${groupId}/invite/`;
    try {
      for (const user of foundUsers) {
        await POST(formURL + user.username, session.user.token, true);
      }
      setResultModal('Invitacion enviada con exito');
      onClose();
    } catch (error) {
      setResultModal('Error al enviar la invitacion');
    }
  };

  // Erase user from foundUsers
  const eraseUser = (user) => {
    setFoundUsers((users) => users.filter((posUser) => posUser.username !== user.username));
  };

  const renderModalContent = (onClose) => (
    <>
      <ModalHeader className="uppercase text-3xl">Enviar invitaciones</ModalHeader>
      <ModalBody className="">
        <Input
          label="Buscar usuario"
          placeholder="Introduzca el nombre del usuario que quiere invitar"
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
        {foundUsers.length !== 0 && (
          <div className="flex flex-col gap-3 ">
            {foundUsers.map((user) => (
              <div key={user.username} className="flex flex-row justify-between gap-5">
                <User
                  name={user.username}
                  avatarProps={{
                    src: user.image,
                  }}
                />
                <Button color="danger" variant="bordered" onClick={() => eraseUser(user)}>
                  Eliminar usuario
                </Button>
              </div>
            ))}
          </div>
        )}
      </ModalBody>
      <ModalFooter>
        <Button color="danger" onPress={onClose}>
          Cancelar
        </Button>
        <Button color="primary" onPress={() => invite(onClose)} isDisabled={foundUsers.length < 1}>
          {loading ? <Spinner color="default" /> : 'Invitar'}
        </Button>
      </ModalFooter>
    </>
  );

  return (
    <>
      <button className="min-h-10 w-full p-1" onClick={onOpen}>
        <p className="w-full h-full flex items-center justify-center leading-tight bg-blue-400/20 rounded-full hover:bg-green-600">Invitar</p>
      </button>
      <Modal isOpen={isOpen} size={'3xl'} onOpenChange={onOpenChange} placement="top-center" scrollBehavior="inside">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
