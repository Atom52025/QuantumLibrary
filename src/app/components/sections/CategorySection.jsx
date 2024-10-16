'use client';

import { Divider } from '@nextui-org/react';
import { useEffect, useState } from 'react';
import { IoIosArrowDropdown, IoIosArrowDropup } from 'react-icons/io';

import Link from 'next/link';

export default function CategorySection() {
  const [isOpen, setIsOpen] = useState(false);
  const [isLgScreen, setIsLgScreen] = useState(false);

  // Checks if window is lg
  useEffect(() => {
    // Check screen width on mount
    const handleResize = () => {
      setIsLgScreen(window.innerWidth >= 1024);
    };
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <div className={`lg:h-full lg:min-w-[220px] lg:w-auto w-full bg-gray-800/50 shadow-inner flex flex-col relative lg:p-0 p-3 ´ ${isOpen || isLgScreen ? 'max-h-[100%]' : 'max-h-12'}`}>
      <button onClick={() => setIsOpen(!isOpen)} className="uppercase text-center text-lg justify-center flex flex-row gap-3 items-center lg:hidden">
        Categorías
        {isOpen ? <IoIosArrowDropup /> : <IoIosArrowDropdown />}
      </button>

      <div className="flex flex-col transition-all duration-300 ease-in-out overflow-hidden uppercase lg:text-2xl text-lg">
        {!isLgScreen && <Divider className="my-4" />}
        <Link href={'all/'} className="text-center items-center p-3 hover:bg-black/50 lg:mt-5">
          Todos
        </Link>
        <Divider className="my-4" />
        <Link href={'favorite/'} className="text-center items-center p-3 hover:bg-black/50">
          Favoritos
        </Link>
        <Link href={'finished/'} className="text-center items-center p-3 hover:bg-black/50">
          Terminados
        </Link>
        <Link href={'completed/'} className="text-center items-center p-3 t hover:bg-black/50">
          Completados
        </Link>
        <Divider className="my-4" />
        <Link href={'backlog1/'} className="text-center items-center p-3 hover:bg-black/50">
          Backlog 1
        </Link>
        <Link href={'backlog2/'} className="text-center items-center p-3 hover:bg-black/50">
          Backlog 2
        </Link>
        <Link href={'backlog3/'} className="text-center items-center p-3 hover:bg-black/50">
          Backlog 3
        </Link>
      </div>
    </div>
  );
}
