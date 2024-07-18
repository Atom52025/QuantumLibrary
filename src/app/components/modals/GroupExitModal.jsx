import { Button, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, Spinner, User, useDisclosure } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useState } from 'react';
import { PiArrowBendDownRightBold } from 'react-icons/pi';

import { GET, POST } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';

export default function GroupExitModal({ groupId }) {
  // Get Session
  const { data: session } = useSession({ required: true });

  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Modal state
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  const exit = async (onClose) => {
    const formURL = `api/user/${session.user.username}/groups/${groupId}`;

    try {
      const res = await DELETE(formURL, session.user.token, requestBody);
      setGroups((prevGroups) => [...prevGroups, res]);
      setResultModal('Group created successfully');
      onClose();
    } catch (error) {
      setResultModal('Error inviting user to group');
    }
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
        <Button color="primary" onPress={() => invite(onClose)}>
          {loading ? <Spinner color="default" /> : 'Invitar'}
        </Button>
      </ModalFooter>
    </>
  );

  return (
    <>
      <button className="h-[4%] w-full p-1" onClick={onOpen}>
        <p className="w-full h-full flex items-center justify-center leading-tight bg-blue-400/20 rounded-full hover:bg-red-600 ">Salir del grupo</p>
      </button>
      <Modal isOpen={isOpen} size={'3xl'} onOpenChange={onOpenChange} placement="top-center" scrollBehavior="inside">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
