import { Checkbox, CheckboxGroup, Divider } from '@nextui-org/react';

import Link from 'next/link';

import { getServerSession } from 'next-auth/next';

export default async function GamesLayout({ children }) {
  return (
    <section className="h-full w-full flex flex-row relative">
      <div className="h-full min-w-[220px] bg-gray-800/50 shadow-inner flex flex-col">
        <Link href={'all/'} className="uppercase text-center items-center p-3 text-2xl hover:bg-black/50 mt-5">
          Todos
        </Link>
        <Divider className="my-4" />
        <Link href={'favorite/'} className="uppercase text-center items-center p-3 text-2xl hover:bg-black/50">
          Favoritos
        </Link>
        <Link href={'finished/'} className="uppercase text-center items-center p-3 text-2xl hover:bg-black/50">
          Terminados
        </Link>
        <Link href={'completed/'} className="uppercase text-center items-center p-3 text-2xl hover:bg-black/50">
          Completados
        </Link>
        <Divider className="my-4" />
        <Link href={'backlog1/'} className="uppercase text-center items-center p-3 text-2xl hover:bg-black/50">
          Backlog 1
        </Link>
        <Link href={'backlog2/'} className="uppercase text-center items-center p-3 text-2xl hover:bg-black/50">
          Backlog 2
        </Link>
        <Link href={'backlog3/'} className="uppercase text-center items-center p-3 text-2xl hover:bg-black/50">
          Backlog 3
        </Link>
      </div>

      {children}
    </section>
  );
}
