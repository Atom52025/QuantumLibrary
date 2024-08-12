'use client';

import { Input, Popover, PopoverContent, PopoverTrigger, User } from '@nextui-org/react';
import { signIn, signOut, useSession } from 'next-auth/react';

import { Button } from '@nextui-org/button';

export default function UserMenu() {
  // Get Session
  const { data: session, status } = useSession();

  const notAuthenticated = () => {
    return status === 'loading' ? (
      <p>Loading...</p>
    ) : (
      <div className="flex flex-row gap-3">
        <Input type="button" className="w-1/12 border border-2 border-gray-500 rounded-xl" value="Iniciar Sesión" onClick={() => signIn()} />
        <Input type="button" className="w-1/12 border border-2 border-gray-500 rounded-xl" value="Registrarse" onClick={() => createUser()} />
      </div>
    );
  };

  const profile = () => {
    window.location.href = '/profile';
  };

  return (
    <>
      {status === 'authenticated' ? (
        <Popover showArrow placement="bottom">
          <PopoverTrigger>
            <User
              name={session.user.username}
              avatarProps={{
                src: session.user.image,
              }}
            />
          </PopoverTrigger>
          <PopoverContent className="p-1 gap-1">
            <Button className="w-full" onClick={() => profile()}>
              Perfil
            </Button>
            <Button className="w-full" onClick={() => signOut()}>
              Cerrar Sesión
            </Button>
          </PopoverContent>
        </Popover>
      ) : (
        notAuthenticated()
      )}
    </>
  );
}
