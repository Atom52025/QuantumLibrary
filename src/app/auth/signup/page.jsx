'use client';

import { signOut, useSession } from 'next-auth/react';
import { signIn } from 'next-auth/react';
import { useEffect, useState } from 'react';

import { POST } from '@/app/api/request';
import { Button } from '@nextui-org/button';

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
  }, [confirmPassword]);

  const register = async () => {
    const formURL = `api/users`;

    if (!username || !email || !password || !confirmPassword) {
      setError('Complete todos los campos');
      return;
    }

    if (password !== confirmPassword) {
      setError('Las contraseñas no coinciden');
      return;
    }

    const requestBody = {
      username: username,
      password: password,
      email: email,
    };

    try {
      await POST(formURL, requestBody);
      await signIn('credentials', { password: password, username: username });
    } catch (error) {
      setError('Error al registrarse, intentelo de nuevo mas tarde');
    }
  };
  if (status === 'authenticated')
    return (
      <div className="flex items-center justify-center w-full h-full flex-col gap-3">
        <p className="text-2xl">Ya tiene una sesion abierta, para crear una cuenta debera cerrarla primero</p>
        <Button
          onClick={() => signOut()}
          className="bg-red-500 w-1/2 text-white py-2 px-4 rounded-md disabled:bg-gray-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-opacity-50">
          Cerrar Sesión
        </Button>
      </div>
    );
  else
    return (
      <div className="flex items-center justify-center min-h-screen bg-gray-900">
        <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-80">
          <div className="mb-4">
            <label className="block text-gray-400 text-sm mb-2" htmlFor="username">
              Nombre de usuario
            </label>
            <input
              id="username"
              name="username"
              type="text"
              className="w-full px-3 py-2 text-gray-900 rounded-md bg-gray-200 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>

          <div className="mb-4">
            <label className="block text-gray-400 text-sm mb-2" htmlFor="username">
              Email
            </label>
            <input
              id="username"
              name="username"
              type="text"
              className="w-full px-3 py-2 text-gray-900 rounded-md bg-gray-200 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <div className="mb-6 gap-2 flex flex-col">
            <label className="block text-gray-400 text-sm mb-2" htmlFor="password">
              Contraseña
            </label>
            <input
              id="password"
              name="password"
              type="password"
              className="w-full px-3 py-2 text-gray-900 rounded-md bg-gray-200 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              placeholder="Contraseña"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <input
              id="password"
              name="password"
              type="password"
              className="w-full px-3 py-2 text-gray-900 rounded-md bg-gray-200 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              placeholder="Confirmar Contraseña"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
            {error && <p className="text-red-500 text-xs italic">{error}</p>}
          </div>

          <div>
            <button
              onClick={() => register()}
              disabled={error.length !== 0}
              className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md disabled:bg-gray-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-opacity-50">
              Registrarse
            </button>
          </div>
        </div>
      </div>
    );
}
