'use client';

import { Button, Card, Input, p } from '@nextui-org/react';
import { signIn, signOut, useSession } from 'next-auth/react';
import { useEffect, useState } from 'react';

import { useRouter } from 'next/navigation';

// Import useRouter for navigation

export default function Page() {
  const { data: session, status } = useSession();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSignIn = async () => {
    if (!username || !password) {
      setError('Por favor, complete todos los campos.');
      return;
    }

    try {
      const result = await signIn('credentials', { username, password, redirect: false });
      if (result?.error) {
        setError('Error al iniciar sesión. Verifique sus credenciales.');
      } else {
        window.location.href = '/';
      }
    } catch (error) {
      setError('Ocurrió un error al intentar iniciar sesión. Intentelo de nuevo más tarde.');
    }
  };

  // If authenticated, show a message and sign-out button
  if (status === 'authenticated') {
    return (
      <div className="flex items-center justify-center w-full h-full flex-col gap-3">
        <p h3>Ya tiene una sesión abierta.</p>
        <Button onClick={() => signOut()} color="error" className="w-1/2">
          Cerrar Sesión
        </Button>
      </div>
    );
  }

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-900">
      <Card className="bg-gray-800 p-6 w-80 flex flex-col gap-4">
        <p className="text-center text-white text-xl">Iniciar Sesión</p>

        <Input clearable underlined label="Nombre de usuario" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />

        <Input clearable underlined label="Contraseña" type="password" placeholder="Contraseña" value={password} onChange={(e) => setPassword(e.target.value)} />

        {error && <p className="text-red-600 text-xs text-center">{error}</p>}

        <Button color="primary" onClick={handleSignIn} disabled={error.length !== 0} className="w-full">
          Iniciar Sesión
        </Button>

        <div className="flex flex-row items-center justify-center gap-2">
          <div className="border-1 border-white flex-grow " />
          <p>o</p>
          <div className="border-1 border-white flex-grow" />
        </div>

        <p className="text-center text-white text-xl">¿No tienes una cuenta?</p>
        <Button variant="ghost" color="primary" onClick={() => (window.location.href = '/auth/signup')} className="w-full">
          Regístrate
        </Button>
      </Card>
    </div>
  );
}
