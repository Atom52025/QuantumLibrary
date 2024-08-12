'use client';

import { Avatar, Card, CardBody, CardFooter, CardHeader, Divider, Input, Spinner } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';

import { PATCH } from '@/app/api/tokenRequest';
import InfoPopups from '@/app/components/InfoPopups';
import ChangePasswordModal from '@/app/components/modals/ChangePasswordModal';
import { Button } from '@nextui-org/button';

export default function UserSection() {
  // Get Session
  const { data: session, update, status } = useSession();

  // Result modal state
  const [resultModal, setResultModal] = useState('closed');

  // User data state
  const [email, setEmail] = useState('');
  const [image, setImage] = useState('');
  const [isEditMode, setIsEditMode] = useState(false);

  // Use useEffect to update the state once the session is loaded
  useEffect(() => {
    if (status === 'authenticated' && session) {
      setEmail(session.user.email);
      setImage(session.user.image);
    }
  }, [session, status]);

  const handleEdit = () => {
    setIsEditMode(true);
  };

  const sendRequest = async () => {
    const formURL = `api/users/${session.user.username}`;

    const requestBody = {
      email: email,
      image: image,
    };

    try {
      await PATCH(formURL, session.user.token, requestBody);
      setResultModal('Informacion del usuario editada con exito');
      await update({ ...session, user: { ...session?.user, email: email, image: image } });
      console.log(session);
      setIsEditMode(false);
    } catch (error) {
      setResultModal('Error al editar la informacion del usuario');
    }
  };

  if (status === 'loading')
    return (
      <main className="min-h-full w-full shadow-inner flex items-center justify-center">
        <Spinner />
      </main>
    );

  return (
    <section className="h-full w-full items-center relative p-5 flex flex-col">
      <h1 className="text-5xl w-full text-center mb-5 uppercase">Perfil</h1>
      <Card className="w-1/2 p-2 bg-gray-600/20">
        <CardHeader>
          <h2 className="text-2xl">Información</h2>
        </CardHeader>
        <Divider />
        <CardBody className="p-2 gap-5">
          <div className="w-full flex flex-row justify-evenly">
            <div className="flex flex-col justify-center items-center h-full aspect-square ">
              <Avatar className="h-[200px] w-auto" src={image} />
            </div>
            <div className="flex flex-col gap-4 p-2 h-fit w-1/2 ">
              <Input label="Nombre de usuario" value={session.user.username} variant="bordered" disabled />
              <Input label="Correo" value={email} placeholder="No hay ningún email definido" variant="bordered" disabled={!isEditMode} onChange={(e) => setEmail(e.target.value)} />
              <Input label="Password actual" value={undefined} variant="bordered" placeholder="*********" disabled />
              {isEditMode && <Input label="New image" variant="bordered" placeholder="https://..." onChange={(e) => setImage(e.target.value)} />}
            </div>
          </div>
        </CardBody>
        <Divider />
        <CardFooter className="justify-end gap-3">
          {isEditMode ? (
            <>
              <Button color="error" onClick={() => setIsEditMode(false)}>
                Cancelar
              </Button>
              <ChangePasswordModal user={session.user} />
              <Button color="success" onClick={() => sendRequest()}>
                Guardar
              </Button>
            </>
          ) : (
            <Button onClick={handleEdit}>Editar Información</Button>
          )}
        </CardFooter>
      </Card>
      <InfoPopups resultModal={resultModal} setResultModal={setResultModal} />
    </section>
  );
}
