import { Button, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, Spinner, User, useDisclosure } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useState } from 'react';
import { PiArrowBendDownRightBold } from 'react-icons/pi';

import { GET, POST } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';

export default function GroupCreateModal({ setGroups }) {
  // Get Session
  const { data: session } = useSession({ required: true });

  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Modal state
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  const [userId, setUserId] = useState('');
  const [name, setName] = useState('');
  const [foundUsers, setFoundUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  const searchUser = async () => {
    const formURL = `api/user/${userId}`;
    try {
      setLoading(true);
      const res = await GET(formURL, session.user.token);
      setLoading(false);

      if (res === null) {
        throw new Error('Failed to fetch data');
      }

      setFoundUsers((prevFoundUsers) => [...prevFoundUsers, res]);
    } catch (error) {
      setResultModal('Error al buscar el usuario');
    }
  };

  const createGroup = async (onClose) => {
    const formURL = `api/user/groups`;

    const requestBody = {
      name: name,
      invited_users: foundUsers.map((user) => user.username),
    };

    try {
      const res = await POST(formURL, session.user.token, requestBody);

      setGroups((prevGroups) => ({
        ...prevGroups,
        accepted: [...prevGroups.accepted, res],
      }));

      setResultModal('Group creado con exito');
      onClose();
    } catch (error) {
      setResultModal('Error al crear grupo');
    }
  };

  const renderModalContent = (onClose) => (
    <>
      <ModalHeader className="uppercase text-3xl">Create Group</ModalHeader>
      <ModalBody className="">
        <Input label="Nombre del grupo" type="text" variant="bordered" value={name} onValueChange={setName} />
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
          <div className="flex flex-col gap-10 ">
            {foundUsers.map((user) => (
              <User
                key={user.username}
                name={user.username}
                avatarProps={{
                  src: user.image,
                }}
              />
            ))}
          </div>
        )}
      </ModalBody>
      <ModalFooter>
        <Button color="danger" onPress={onClose}>
          Cancel
        </Button>
        <Button color="primary" onPress={() => createGroup(onClose)}>
          {loading ? <Spinner color="default" /> : 'Crear Grupo'}
        </Button>
      </ModalFooter>
    </>
  );

  return (
    <>
      <button className="min-h-10 w-full p-1" onClick={onOpen}>
        <p className="w-full h-full flex items-center justify-center leading-tight bg-blue-400/20 rounded-full hover:bg-blue-600 ">Crear Grupo</p>
      </button>
      <Modal isOpen={isOpen} size={'3xl'} onOpenChange={onOpenChange} placement="top-center" scrollBehavior="inside">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
