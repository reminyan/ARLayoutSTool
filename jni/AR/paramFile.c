/*******************************************************
 *
 * Author: Takeshi Mita, Shinsaku Hiura, Hirokazu Kato
 *
 *         tmita@inolab.sys.es.osaka-u.ac.jp
 *         shinsaku@sys.es.osaka-u.ac.jp
 *         kato@sys.im.hiroshima-cu.ac.jp
 *
 * Revision: 4.1
 * Date: 01/12/07
 *
*******************************************************/
// HACK by r
#include <stdio.h>
#include <stdarg.h>
#include <AR/param.h>
/* va_list‘Îô */
    typedef __builtin_va_list va_list;
    #define va_start(v,l)   __builtin_va_start(v,l)
    #define va_end(v)   __builtin_va_end(v)
    #define va_arg(v,l) __builtin_va_arg(v,l)
    #if !defined(__STRICT_ANSI__) || __STDC_VERSION__ + 0 >= 199900L || defined(__GXX_EXPERIMENTAL_CXX0X__)
    #define va_copy(d,s)    __builtin_va_copy(d,s)
    #endif
    #define __va_copy(d,s)  __builtin_va_copy(d,s)
    typedef __builtin_va_list __gnuc_va_list;
    typedef __gnuc_va_list va_list;
    typedef va_list __va_list;

#ifdef AR_BIG_ENDIAN
#error USING BIG_ENDIAN, but ARM is little endian ... config is wrong
#endif

#ifdef AR_LITTLE_ENDIAN
typedef union {
	int  x;
	unsigned char y[4];
} SwapIntT;

typedef union {
	double  x;
	unsigned char y[8];
} SwapDoubleT;

static void byteSwapInt( int *from, int *to )
{
    SwapIntT   *w1, *w2;
    int        i;

    w1 = (SwapIntT *)from;
    w2 = (SwapIntT *)to;
    for( i = 0; i < 4; i++ ) {
        w2->y[i] = w1->y[3-i];
    }

    return;
}

static void byteSwapDouble( double *from, double *to )
{
    SwapDoubleT   *w1, *w2;
    int           i;

    w1 = (SwapDoubleT *)from;
    w2 = (SwapDoubleT *)to;
    for( i = 0; i < 8; i++ ) {
        w2->y[i] = w1->y[7-i];
    }

    return;
}

static void byteswap( ARParam *param )
{
    ARParam  wparam;
    int      i, j;

    byteSwapInt( &(param->xsize), &(wparam.xsize) );
    byteSwapInt( &(param->ysize), &(wparam.ysize) );

    for( j = 0; j < 3; j++ ) {
        for( i = 0; i < 4; i++ ) {
            byteSwapDouble( &(param->mat[j][i]), &(wparam.mat[j][i]) );
        }
    }

    for( i = 0; i < 4; i++ ) {
        byteSwapDouble( &(param->dist_factor[i]), &(wparam.dist_factor[i]) );
    }

    *param = wparam;
}

static void byteswap2( ARSParam *sparam )
{
    ARSParam wsparam;
    int      i, j;

    byteSwapInt( &(sparam->xsize), &(wsparam.xsize) );
    byteSwapInt( &(sparam->ysize), &(wsparam.ysize) );

    for( j = 0; j < 3; j++ ) {
        for( i = 0; i < 4; i++ ) {
            byteSwapDouble( &(sparam->matL[j][i]),   &(wsparam.matL[j][i]) );
            byteSwapDouble( &(sparam->matR[j][i]),   &(wsparam.matR[j][i]) );
            byteSwapDouble( &(sparam->matL2R[j][i]), &(wsparam.matL2R[j][i]) );
        } 
    }
    for( i = 0; i < 4; i++ ) {
        byteSwapDouble( &(sparam->dist_factorL[i]), &(wsparam.dist_factorL[i]) );
        byteSwapDouble( &(sparam->dist_factorR[i]), &(wsparam.dist_factorR[i]) );
    }

    *sparam = wsparam;
}
#endif


int    arParamSave( char *filename, int num, ARParam *param, ...)
{
    FILE        *fp;
    va_list     ap;
    ARParam     *param1;
    int         i;

    if( num < 1 ) return -1;

    fp = fopen( filename, "wb" );
    if( fp == NULL ) return -1;

#ifdef AR_LITTLE_ENDIAN
    byteswap( param );
#endif
    if( fwrite( param, sizeof(ARParam), 1, fp ) != 1 ) {
        fclose(fp);
#ifdef AR_LITTLE_ENDIAN
        byteswap( param );
#endif
        return -1;
    }
#ifdef AR_LITTLE_ENDIAN
    byteswap( param );
#endif

    va_start(ap, param);
    for( i = 1; i < num; i++ ) {
        param1 = va_arg(ap, ARParam *);
#ifdef AR_LITTLE_ENDIAN
        byteswap( param1 );
#endif
        if( fwrite( param1, sizeof(ARParam), 1, fp ) != 1 ) {
            fclose(fp);
#ifdef AR_LITTLE_ENDIAN
            byteswap( param1 );
#endif
            return -1;
        }
#ifdef AR_LITTLE_ENDIAN
        byteswap( param1 );
#endif
    }

    fclose(fp);

    return 0;
}

int    arParamLoad( const char *filename, int num, ARParam *param, ...)
{
    FILE        *fp;
    va_list     ap;
    ARParam     *param1;
    int         i;

    if( num < 1 ) return -1;

    fp = fopen( filename, "rb" );
    if( fp == NULL ) return -1;

    if( fread( param, sizeof(ARParam), 1, fp ) != 1 ) {
        fclose(fp);
        return -1;
    }
#ifdef AR_LITTLE_ENDIAN
    byteswap( param );
#endif

    va_start(ap, param);
    for( i = 1; i < num; i++ ) {
        param1 = va_arg(ap, ARParam *);
        if( fread( param1, sizeof(ARParam), 1, fp ) != 1 ) {
            fclose(fp);
            return -1;
        }
#ifdef AR_LITTLE_ENDIAN
        byteswap( param1 );
#endif
    }

    fclose(fp);

    return 0;
}

int    arsParamSave( char *filename, ARSParam *sparam )
{   
    FILE        *fp;

    fp = fopen( filename, "wb" );
    if( fp == NULL ) return -1;

#ifdef AR_LITTLE_ENDIAN
    byteswap2( sparam );
#endif
    if( fwrite( sparam, sizeof(ARSParam), 1, fp ) != 1 ) {
        fclose(fp);
#ifdef AR_LITTLE_ENDIAN
        byteswap2( sparam );
#endif
        return -1;
    }
#ifdef AR_LITTLE_ENDIAN
    byteswap2( sparam );
#endif

    fclose(fp);

    return 0;
}

int    arsParamLoad( char *filename, ARSParam *sparam )
{
    FILE        *fp;

    fp = fopen( filename, "rb" );
    if( fp == NULL ) return -1;

    if( fread( sparam, sizeof(ARSParam), 1, fp ) != 1 ) {
        fclose(fp);
        return -1;
    }
#ifdef AR_LITTLE_ENDIAN
    byteswap2( sparam );
#endif

    fclose(fp);

    return 0;
}
