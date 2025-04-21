'use client';

import { Card, CardBody } from '@nextui-org/react';
import { useEffect, useState } from 'react';

import { motion } from 'framer-motion';
import { AnimatePresence } from 'framer-motion';

export default function InfoPopups({ resultModal, setResultModal }) {
  const [popupColor, setPopupColor] = useState('bg-green-500');

  useEffect(() => {
    if (resultModal.toLowerCase().includes('error')) {
      setPopupColor('bg-red-600');
    } else if (resultModal.toLowerCase().includes('warning')) {
      setPopupColor('bg-amber-500');
    } else {
      setPopupColor('bg-green-500');
    }

    const timer = setTimeout(() => {
      setResultModal('closed');
    }, 5000);

    return () => clearTimeout(timer);
  }, [resultModal]);

  return (
    <AnimatePresence>
      {resultModal !== 'closed' && (
        <motion.div
          initial={{ opacity: 0, y: 100 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: 1000 }}
          transition={{ ease: 'easeInOut', duration: 1 }}
          className="fixed h-[100vh] w-[100vw] top-0 left-0 pointer-events-none z-50 flex justify-center">
          <Card className={`text-gray-900 xl:text-3xl md:text-xl text-lg absolute bottom-3 ${popupColor}`}>
            <CardBody className=" ">
              <p>{resultModal}</p>
            </CardBody>
          </Card>
        </motion.div>
      )}
    </AnimatePresence>
  );
}
