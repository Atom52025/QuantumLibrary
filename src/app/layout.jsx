import { Inter } from 'next/font/google';
import Link from 'next/link';

import { Providers } from './providers';
import './styles/globals.css';
import Navbar from '@/app/components/sections/Navbar';

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
          <Navbar/>
          <div className="lg:h-[91vh] h-[95vh] overflow-hidden bg-gray-900 font-russo">{children}</div>
        </Providers>
      </body>
    </html>
  );
}
