'use client';

import { Divider } from '@nextui-org/react';
import { useEffect, useState } from 'react';
import { IoIosArrowDropdown, IoIosArrowDropup } from 'react-icons/io';

import { revalidatePath, revalidateTag } from 'next/cache';
import Link from 'next/link';
import { usePathname } from 'next/navigation';

export default function CategorySection() {
  const [isOpen, setIsOpen] = useState(false);
  const [isLgScreen, setIsLgScreen] = useState(true);
  const pathname = usePathname();

  // Helper function to check if the link is active
  const isActive = (path) => pathname === path;

  console.log(pathname);

  // Checks if window is lg
  useEffect(() => {
    // Check screen width on mount
    const handleResize = () => {
      setIsLgScreen(window.innerWidth >= 768);
    };
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <div className={`md:h-full md:min-w-[220px] md:w-auto w-full bg-gray-800/50 shadow-inner flex flex-col relative md:p-0 p-3 ´ ${isOpen || isLgScreen ? 'max-h-[100%]' : 'max-h-12'}`}>
      <button onClick={() => setIsOpen(!isOpen)} className="uppercase text-center text-lg justify-center flex flex-row gap-3 items-center md:hidden">
        Categorías
        {isOpen ? <IoIosArrowDropup /> : <IoIosArrowDropdown />}
      </button>

      <div className="flex flex-col transition-all duration-300 ease-in-out overflow-hidden uppercase md:text-2xl text-md">
        {!isLgScreen && <Divider className="my-4" />}
        <Link
          href={'all/'}
          className={`text-center items-center p-3 hover:bg-black/50 md:mt-5 ${isActive('/library/games/all') ? 'text-blue-500' : 'text-white'}`}
          onClick={() => revalidateTag('games')}>
          Todos
        </Link>
        <Divider className="my-4" />
        <Link
          href={'favorite/'}
          className={`text-center items-center p-3 hover:bg-black/50 ${isActive('/library/games/favorite') ? 'text-blue-500' : 'text-white'}`}
          onClick={() => revalidateTag('games')}>
          Favoritos
        </Link>
        <Link
          href={'finished/'}
          className={`text-center items-center p-3 hover:bg-black/50 ${isActive('/library/games/finished') ? 'text-blue-500' : 'text-white'}`}
          onClick={() => revalidatePath('/')}>
          Terminados
        </Link>
        <Link
          href={'completed/'}
          className={`text-center items-center p-3 hover:bg-black/50 ${isActive('/library/games/completed') ? 'text-blue-500' : 'text-white'}`}
          onClick={() => revalidatePath('/')}>
          Completados
        </Link>
        <Divider className="my-4" />
        <Link
          href={'backlog1/'}
          className={`text-center items-center p-3 hover:bg-black/50 ${isActive('/library/games/backlog1') ? 'text-blue-500' : 'text-white'}`}
          onClick={() => revalidatePath('/')}>
          Backlog 1
        </Link>
        <Link
          href={'backlog2/'}
          className={`text-center items-center p-3 hover:bg-black/50 ${isActive('/library/games/backlog2') ? 'text-blue-500' : 'text-white'}`}
          onClick={() => revalidatePath('/')}>
          Backlog 2
        </Link>
        <Link
          href={'backlog3/'}
          className={`text-center items-center p-3 hover:bg-black/50 ${isActive('/library/games/backlog3') ? 'text-blue-500' : 'text-white'}`}
          onClick={() => revalidatePath('/')}>
          Backlog 3
        </Link>
      </div>
    </div>
  );
}
