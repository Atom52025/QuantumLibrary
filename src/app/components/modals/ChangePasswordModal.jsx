'use client';

import { Button, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, useDisclosure } from '@nextui-org/react';
import { useState } from 'react';

import { PATCH } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';

export default function ChangePasswordModal({ user }) {
  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // Password state
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [passwordError, setPasswordError] = useState('');

  // Modal state
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  const checkPasswords = (onClose) => {
    if (newPassword !== confirmPassword) {
      setPasswordError('Las contraseñas no coinciden');
      return false;
    }
    if (newPassword.length < 6) {
      setPasswordError('La contraseña debe tener al menos 6 caracteres');
      return false;
    }

    sendRequest(onClose);
    return true;
  };

  const sendRequest = async (onClose) => {
    const formURL = `api/users/${user.username}/password`;

    const requestBody = {
      oldPassword: oldPassword,
      newPassword: newPassword,
    };

    try {
      await PATCH(formURL, user.token, requestBody);
      setResultModal('Contraseña cambiada con exito');
      onClose();
    } catch (error) {
      setResultModal('Error al cambiar la contraseña');
    }
  };

  const renderModalContent = (onClose) => (
    <>
      <ModalHeader>Cambiar Contraseña</ModalHeader>
      <ModalBody>
        <Input label="Contraseña Actual" type="password" value={oldPassword} onChange={(e) => setOldPassword(e.target.value)} placeholder="Ingrese su contraseña actual" variant="bordered" fullWidth />
        <Input label="Nueva Contraseña" type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} placeholder="Ingrese su nueva contraseña" variant="bordered" fullWidth />
        <Input
          label="Confirmar Nueva Contraseña"
          type="password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          placeholder="Confirme su nueva contraseña"
          variant="bordered"
          fullWidth
        />
        {passwordError && <p className="text-red-500">{passwordError}</p>}
      </ModalBody>
      <ModalFooter>
        <Button color="error" onClick={onClose}>
          Cancelar
        </Button>
        <Button color="success" onClick={checkPasswords(onClose)}>
          Guardar Cambios
        </Button>
      </ModalFooter>
    </>
  );

  return (
    <>
      <Button color="primary" onClick={onOpen}>
        Cambiar Contraseña
      </Button>
      <Modal isOpen={isOpen} size={'3xl'} onOpenChange={onOpenChange} placement="top-center">
        <ModalContent>{(onClose) => renderModalContent(onClose)}</ModalContent>
      </Modal>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </>
  );
}
