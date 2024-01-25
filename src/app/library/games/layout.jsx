import { Checkbox, CheckboxGroup } from '@nextui-org/react';

import Link from 'next/link';

export default function GamesLayout({ children }) {
  return (
    <section className="h-full w-full flex flex-row relative">
      <div className="h-full min-w-[200px] bg-gray-800/50 shadow-inner flex flex-col">
        <Link href={'games?category=all'} className="uppercase text-center items-center p-3 text-2xl hover:bg-black/50">
          Todos
        </Link>
      </div>

      {children}
    </section>
  );
}
