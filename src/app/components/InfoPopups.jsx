'use client';

import { Card, CardBody } from '@nextui-org/react';
import { useEffect, useState } from 'react';

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
    <>
      {resultModal !== 'closed' && (
        <Card className={`text-gray-900 text-3xl absolute bottom-3 right-3 z-[100] ${popupColor}`}>
          <CardBody>
            <p>{resultModal}</p>
          </CardBody>
        </Card>
      )}
    </>
  );
}
