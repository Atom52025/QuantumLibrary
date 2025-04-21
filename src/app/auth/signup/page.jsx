'use client';

import { Button, p } from '@nextui-org/react';
import { Input } from '@nextui-org/react';
import { Card } from '@nextui-org/react';
import { signOut, useSession } from 'next-auth/react';
import { signIn } from 'next-auth/react';
import { useEffect, useState } from 'react';

import { POST } from '@/app/api/request';

export default function Page() {
  const { data: session, status } = useSession();

  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    if (confirmPassword !== password) {
      setError('Las contraseñas no coinciden');
    } else {
      setError('');
    }
  }, [confirmPassword, password]);

  const register = async () => {
    const formURL = `api/signup`;

    if (!username || !email || !password || !confirmPassword) {
      setError('Complete todos los campos');
      return;
    }

    if (password !== confirmPassword) {
      setError('Las contraseñas no coinciden');
      return;
    }

    const requestBody = {
      username,
      password,
      email,
    };

    try {
      await POST(formURL, requestBody);
      await signIn('credentials', { username: username, password: password });

      window.location.href = '/';
    } catch (error) {
      setError('Error al registrarse, intentelo de nuevo más tarde');
    }
  };

  if (status === 'authenticated')
    return (
      <div className="flex items-center justify-center w-full h-full flex-col gap-3">
        <h3>Ya tiene una sesión abierta, para crear una cuenta deberá cerrarla primero</h3>
        <Button onClick={() => signOut()} color="error" className="w-1/2">
          Cerrar Sesión
        </Button>
      </div>
    );

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-900">
      <Card className="bg-gray-800 p-6 w-80 flex flex-col gap-4">
        <h3 className="text-center text-white text-xl">Registrarse</h3>

        <Input clearable underlined label="Nombre de usuario" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />

        <Input clearable underlined label="Email" type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />

        <Input clearable underlined label="Contraseña" type="password" placeholder="Contraseña" value={password} onChange={(e) => setPassword(e.target.value)} />

        <Input label="Confirmar Contraseña" type="password" placeholder="Confirmar Contraseña" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />

        {error && <p className="text-red-600 text-xs text-center">{error}</p>}

        <Button color="primary" onClick={register} disabled={error.length !== 0} className="w-full">
          Registrarse
        </Button>

        <div className="flex flex-row items-center justify-center gap-2">
          <div className="border-1 border-white flex-grow " />
          <p>o</p>
          <div className="border-1 border-white flex-grow" />
        </div>

        <p className="text-center text-white text-xl">¿Ya tiene una cuenta?</p>
        <Button variant="ghost" color="primary" onClick={() => (window.location.href = '/auth/signin')} className="w-full">
          Iniciar Sesión
        </Button>
      </Card>
    </div>
  );
}
