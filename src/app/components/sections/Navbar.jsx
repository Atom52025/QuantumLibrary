'use client';

import Link from "next/link";
import UserMenu from "@/app/components/user/UserMenu";
import { Navbar as NextUINavbar, NavbarBrand, NavbarContent, NavbarItem, NavbarMenuToggle, NavbarMenu, NavbarMenuItem, Button } from "@nextui-org/react";
import {useState} from "react";
import { signIn, signOut, useSession } from 'next-auth/react';

export default function Navbar() {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const { data: session, status } = useSession();

    return (
        <NextUINavbar maxWidth="full"  className="lg:h-[9vh] h-[5vh] bg-gray-800 items-center align-bottom font-russo py-5 justify-normal relative" onMenuOpenChange={setIsMenuOpen}>

            <NavbarContent className="flex-shrink" justify="start" >
                <NavbarMenuToggle aria-label="Toggle menu" className="lg:hidden" />
                <NavbarBrand className="flex-grow-0">
                    <Link href="/" className="font-faster-stroker lg:text-4xl xl:text-5xl sm:text-2xl text-xl text-white uppercase ">
                        Quantum Library
                    </Link>
                </NavbarBrand>
                <NavbarContent className="hidden lg:flex gap-4" justify="start">
                    <NavbarItem>
                        <h2 className="text-2xl font-white"> | </h2>
                    </NavbarItem>
                    <NavbarItem>
                        <Link href="/games" className="xl:text-2xl text-lg text-white">
                            Explorar
                        </Link>
                    </NavbarItem>
                    <NavbarItem>
                        <h2 className="text-2xl font-white"> | </h2>
                    </NavbarItem>
                    { status === 'authenticated' &&
                        <>
                            <NavbarItem>
                                <Link href="/stats" className="xl:text-2xl text-lg text-white">
                                    Mis Estadísticas
                                </Link>
                            </NavbarItem>
                            <NavbarItem>
                                <h2 className="text-2xl font-white"> | </h2>
                            </NavbarItem>
                            <NavbarItem>
                                <Link href="/library/games/all" className="xl:text-2xl text-lg text-white">
                                    Biblioteca
                                </Link>
                            </NavbarItem>
                        </>
                    }
                </NavbarContent>
            </NavbarContent>



            <NavbarContent className="hidden lg:flex gap-4" justify="end">
                <NavbarItem>
                    <UserMenu />
                </NavbarItem>
            </NavbarContent>

            <NavbarMenu className=" top-[9vh] font-russo gap-2 ">
                <NavbarMenuItem>
                    <Link className="w-full text-center" href="/games">Explorar</Link>
                </NavbarMenuItem>

                {status === 'authenticated' ?
                    <>
                        <NavbarMenuItem>
                            <Link className="w-full text-center mt-2" href="/stats">Mis Estadísticas</Link>
                        </NavbarMenuItem>
                        <NavbarMenuItem>
                            <Link className="w-full text-center" href="/library/games/all">Biblioteca</Link>
                        </NavbarMenuItem>
                        <NavbarMenuItem>
                            <Link className="w-full text-center" href="/profile">Perfil</Link>
                        </NavbarMenuItem>
                        <NavbarMenuItem>
                            <Link className="w-full text-center" href="" onClick={() => signOut()}>Cerrar Sesión</Link>
                        </NavbarMenuItem>
                    </>
                    :
                    <>
                        <NavbarMenuItem>
                            <Link className="w-full text-center" href="" onClick={() => signIn()}>Iniciar Sesión</Link>
                        </NavbarMenuItem>
                        <NavbarMenuItem>
                            <Link className="w-full text-center" href="/auth/signup">Registrarse</Link>
                        </NavbarMenuItem>
                    </>
                }
            </NavbarMenu>
        </NextUINavbar>
    );
}

/**
export default function Navbar () {
    return (
        <div className="justify-between h-[9vh] bg-gray-800 items-center align-bottom font-russo flex flex-row p-5 ">
            <div className="flex flex-row items-center align-bottom h-full gap-5 ">
                <Link href="/" className="font-faster-stroker lg:text-4xl text-xl font-white uppercase ">
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
            <UserMenu/>
        </div>
    )
}
 */
