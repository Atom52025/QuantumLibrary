'use client';

import { NavbarBrand, NavbarContent, NavbarItem, NavbarMenu, NavbarMenuItem, NavbarMenuToggle, Navbar as NextUINavbar } from '@nextui-org/react';
import { signIn, signOut, useSession } from 'next-auth/react';
import { useState } from 'react';

import Link from 'next/link';
import { usePathname } from 'next/navigation';

import UserMenu from '@/app/components/user/UserMenu';

export default function Navbar() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const { data: session, status } = useSession();
  const pathname = usePathname();

  // Helper function to check if the link is active
  const isActive = (path) => pathname === path;

  return (
    <NextUINavbar maxWidth="full" className="lg:h-[9vh] h-[5vh] bg-gray-800 items-center align-bottom font-russo py-5 justify-normal relative" onMenuOpenChange={setIsMenuOpen}>
      <NavbarContent className="flex-shrink" justify="start">
        <NavbarMenuToggle aria-label="Toggle menu" className="lg:hidden" />
        <NavbarBrand className="flex-grow-0">
          <Link href="/" className="font-faster-stroker lg:text-4xl xl:text-5xl sm:text-2xl text-xl text-white uppercase">
            Quantum Library
          </Link>
        </NavbarBrand>
        <NavbarContent className="hidden lg:flex gap-4" justify="start">
          <NavbarItem>
            <h2 className="text-2xl font-white"> | </h2>
          </NavbarItem>
          <NavbarItem>
            <Link href="/games" className={`xl:text-2xl text-lg ${isActive('/games') ? 'text-blue-500' : 'text-white'}`}>
              Explorar
            </Link>
          </NavbarItem>
          {status === 'authenticated' && (
            <>
              <NavbarItem>
                <h2 className="text-2xl font-white"> | </h2>
              </NavbarItem>
              <NavbarItem>
                <Link href="/stats" className={`xl:text-2xl text-lg ${isActive('/stats') ? 'text-blue-500' : 'text-white'}`}>
                  Mis Estadísticas
                </Link>
              </NavbarItem>
              <NavbarItem>
                <h2 className="text-2xl font-white"> | </h2>
              </NavbarItem>
              <NavbarItem>
                <Link href="/library/games/all" className={`xl:text-2xl text-lg ${isActive('/library/games/all') ? 'text-blue-500' : 'text-white'}`}>
                  Biblioteca
                </Link>
              </NavbarItem>
            </>
          )}
        </NavbarContent>
      </NavbarContent>

      <NavbarContent className="hidden lg:flex gap-4" justify="end">
        <NavbarItem>
          <UserMenu />
        </NavbarItem>
      </NavbarContent>

      <NavbarMenu className="lg:top-[9vh] top-[5vh] !h-[95vh] font-russo gap-2 z-50">
        <NavbarMenuItem>
          <Link className={`w-full text-center ${isActive('/games') ? 'text-blue-500' : ''}`} href="/games" onClick={() => setIsMenuOpen(false)}>
            Explorar
          </Link>
        </NavbarMenuItem>

        {status === 'authenticated' ? (
          <>
            <NavbarMenuItem>
              <Link className={`w-full text-center ${isActive('/stats') ? 'text-blue-500' : ''}`} href="/stats" onClick={() => setIsMenuOpen(false)}>
                Mis Estadísticas
              </Link>
            </NavbarMenuItem>
            <NavbarMenuItem>
              <Link className={`w-full text-center ${isActive('/library/games/all') ? 'text-blue-500' : ''}`} href="/library/games/all" onClick={() => setIsMenuOpen(false)}>
                Biblioteca
              </Link>
            </NavbarMenuItem>
            <NavbarMenuItem>
              <Link className={`w-full text-center`} href="/profile" onClick={() => setIsMenuOpen(false)}>
                Perfil
              </Link>
            </NavbarMenuItem>
            <NavbarMenuItem>
              <Link className={`w-full text-center`} href="" onClick={() => signOut()}>
                Cerrar Sesión
              </Link>
            </NavbarMenuItem>
          </>
        ) : (
          <>
            <NavbarMenuItem>
              <Link className={`w-full text-center`} href="" onClick={() => signIn()}>
                Iniciar Sesión
              </Link>
            </NavbarMenuItem>
            <NavbarMenuItem>
              <Link className={`w-full text-center`} href="/auth/signup" onClick={() => setIsMenuOpen(false)}>
                Registrarse
              </Link>
            </NavbarMenuItem>
          </>
        )}
      </NavbarMenu>
    </NextUINavbar>
  );
}
