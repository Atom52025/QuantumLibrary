import { Inter } from 'next/font/google';
import Link from 'next/link';

import { Providers } from './providers';
import './styles/globals.css';
import UserMenu from '@/app/components/user/UserMenu';

const inter = Inter({ subsets: ['latin'] });

export const metadata = {
  title: 'Quantum Library',
  description: 'Quantum Library es una plataforma para organizar y descubrir juegos de PC',
};

export default function RootLayout({ children }) {
  return (
    <html lang="en" className="h-full ">
      <head>
        <link rel="icon" href="/favicon.ico" sizes="any" />
        <title>Quantum Library</title>
      </head>

      <body className={inter.className + ' h-full dark font-russo'} lang="es">
        <Providers>
          <div className="justify-between h-[9vh] bg-gray-800 items-center align-bottom font-russo flex flex-row p-5 ">
            <div className="flex flex-row items-center align-bottom h-full gap-5 ">
              <Link href="/" className="font-faster-stroker text-4xl font-white uppercase ">
                Quantum Library
              </Link>
              <h2 className="text-2xl font-white"> | </h2>
              <Link href="/games" className="text-2xl font-white ">
                Explorar
              </Link>
              <h2 className="text-2xl font-white"> | </h2>
              <Link href="/library/games/all" className="text-2xl font-white ">
                Biblioteca
              </Link>
            </div>
            <UserMenu />
          </div>

          <div className="h-[91vh] overflow-hidden bg-gray-900 font-russo">{children}</div>
        </Providers>
      </body>
    </html>
  );
}
