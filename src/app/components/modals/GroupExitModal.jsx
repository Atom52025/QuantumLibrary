import { Button, Modal, ModalContent, ModalFooter, ModalHeader, useDisclosure } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useState } from 'react';

import { DELETE } from '@/app/api/tokenRequest';
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
      await DELETE(formURL, session.user.token);
      setResultModal('Grupo eliminado con exito');
      onClose();
      window.location.href = '/library/games/all';
    } catch (error) {
      setResultModal('Error al eliminar el grupo');
    }
  };

  const renderModalContent = (onClose) => (
    <>
      <ModalHeader className="uppercase text-2xl">¿Estás seguro que deseas salir del grupo?</ModalHeader>
      <ModalFooter>
        <Button color="primary" onPress={onClose}>
          Cancelar
        </Button>
        <Button color="danger" onPress={() => exit(onClose)}>
          Eliminar grupo
        </Button>
      </ModalFooter>
    </>
  );

  return (
    <>
      <button className="min-h-10 w-full p-1" onClick={onOpen}>
        <p className="w-full h-full flex items-center justify-center leading-tight bg-blue-400/20 rounded-full hover:bg-red-600 ">Salir del grupo</p>
      </button>
      <Modal isOpen={isOpen} size={'3xl'} onOpenChange={onOpenChange} placement="top-center" scrollBehavior="inside">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
