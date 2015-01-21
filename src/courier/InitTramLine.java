package courier;

/**
 * Created by daniel on 15/1/20.
 */
public class InitTramLine {
    Map map;
    public InitTramLine(Map map){
        this.map = map;
    }

    public void initTramLine(){
//        map.addTramLine("Bakerloo","Harrow And Wealdstone","Kenton");
//        map.addTramLine("Bakerloo","Paddington","Edgware Road (Bakerloo)");
//        map.addTramLine("Bakerloo","Edgware Road (Bakerloo)","Marylebone");
//        map.addTramLine("Bakerloo","Marylebone","Baker Street");
//        map.addTramLine("Bakerloo","Baker Street","Regent's Park");
//        map.addTramLine("Bakerloo","Regent's Park","Oxford Circus");
//        map.addTramLine("Bakerloo","Oxford Circus","Piccadilly Circus");
//        map.addTramLine("Bakerloo","Piccadilly Circus","Charing Cross");
        map.addTramLine("Bakerloo","Charing Cross","Embankment");
        map.addTramLine("Bakerloo","Embankment","Waterloo");
        map.addTramLine("Bakerloo","Waterloo","Lambeth North");
        map.addTramLine("Bakerloo","Lambeth North","Elephant And Castle");
        map.addTramLine("Bakerloo","Warwick Avenue","Paddington");
        map.addTramLine("Bakerloo","Maida Vale","Warwick Avenue");
        map.addTramLine("Bakerloo","Kenton","South Kenton");
        map.addTramLine("Bakerloo","South Kenton","North Wembley");
        map.addTramLine("Bakerloo","North Wembley","Wembley Central");
        map.addTramLine("Bakerloo","Wembley Central","Stonebridge Park");
        map.addTramLine("Bakerloo","Stonebridge Park","Harlesden");
        map.addTramLine("Bakerloo","Harlesden","Willesden Junction");
        map.addTramLine("Bakerloo","Willesden Junction","Kensal Green");
        map.addTramLine("Bakerloo","Kensal Green","Queen's Park");
        map.addTramLine("Bakerloo","Queen's Park","Kilburn Park");
        map.addTramLine("Bakerloo","Kilburn Park","Maida Vale");
//        map.addTramLine("Central","West Acton","Ealing Broadway");
//        map.addTramLine("Central","North Acton","Hanger Lane");
//        map.addTramLine("Central","Hanger Lane","Perivale");
//        map.addTramLine("Central","Perivale","Greenford");
//        map.addTramLine("Central","Greenford","Northolt");
//        map.addTramLine("Central","Northolt","South Ruislip");
//        map.addTramLine("Central","South Ruislip","Ruislip Gardens");
//        map.addTramLine("Central","Ruislip Gardens","West Ruislip");
//        map.addTramLine("Central","East Acton","North Acton");
//        map.addTramLine("Central","Oxford Circus","Bond Street");
//        map.addTramLine("Central","Leyton","Stratford");
//        map.addTramLine("Central","North Acton","West Acton");
//        map.addTramLine("Central","White City","East Acton");
//        map.addTramLine("Central","Shepherd's Bush","White City");
//        map.addTramLine("Central","Gants Hill","Newbury Park");
//        map.addTramLine("Central","Newbury Park","Barkingside");
//        map.addTramLine("Central","Barkingside","Fairlop");
//        map.addTramLine("Central","Fairlop","Hainault");
//        map.addTramLine("Central","Hainault","Grange Hill");
//        map.addTramLine("Central","Grange Hill","Chigwell");
//        map.addTramLine("Central","Chigwell","Roding Valley");
//        map.addTramLine("Central","Leytonstone","Leyton");
//        map.addTramLine("Central","Stratford","Mile End");
        map.addTramLine("Central","Mile End","Bethnal Green");
        map.addTramLine("Central","Bethnal Green","Liverpool Street");
        map.addTramLine("Central","Liverpool Street","Bank");
        map.addTramLine("Central","Bank","St. Paul's");
        map.addTramLine("Central","St. Paul's","Chancery Lane");
        map.addTramLine("Central","Chancery Lane","Holborn");
        map.addTramLine("Central","Holborn","Tottenham Court Road");
        map.addTramLine("Central","Redbridge","Gants Hill");
        map.addTramLine("Central","Wanstead","Redbridge");
        map.addTramLine("Central","Holland Park","Shepherd's Bush");
        map.addTramLine("Central","Notting Hill Gate","Holland Park");
        map.addTramLine("Central","Queensway","Notting Hill Gate");
        map.addTramLine("Central","Lancaster Gate","Queensway");
        map.addTramLine("Central","Bond Street","Marble Arch");
        map.addTramLine("Central","Marble Arch","Lancaster Gate");
        map.addTramLine("Central","Epping","Theydon Bois");
        map.addTramLine("Central","Theydon Bois","Debden");
        map.addTramLine("Central","Debden","Loughton");
        map.addTramLine("Central","Loughton","Buckhurst Hill");
        map.addTramLine("Central","Buckhurst Hill","Woodford");
        map.addTramLine("Central","Woodford","South Woodford");
        map.addTramLine("Central","South Woodford","Snaresbrook");
        map.addTramLine("Central","Snaresbrook","Leytonstone");
        map.addTramLine("Central","Leytonstone","Wanstead");
        map.addTramLine("Central","Tottenham Court Road","Oxford Circus");
//        map.addTramLine("Circle","King's Cross St. Pancras","Farringdon");
//        map.addTramLine("Circle","South Kensington","Gloucester Road");
//        map.addTramLine("Circle","Aldgate","Tower Hill");
//        map.addTramLine("Circle","Liverpool Street","Aldgate");
//        map.addTramLine("Circle","Moorgate","Liverpool Street");
//        map.addTramLine("Circle","Farringdon","Barbican");
//        map.addTramLine("Circle","Euston Square","King's Cross St. Pancras");
//        map.addTramLine("Circle","Great Portland Street","Euston Square");
//        map.addTramLine("Circle","Baker Street","Great Portland Street");
//        map.addTramLine("Circle","Edgware Road (Hammersmith)","Baker Street");
//        map.addTramLine("Circle","Barbican","Moorgate");
//        map.addTramLine("Circle","High Street Kensington","Notting Hill Gate");
//        map.addTramLine("Circle","Tower Hill","Monument");
//        map.addTramLine("Circle","Monument","Cannon Street");
//        map.addTramLine("Circle","Sloane Square","South Kensington");
//        map.addTramLine("Circle","Victoria","Sloane Square");
        map.addTramLine("Circle","St. James's Park","Victoria");
        map.addTramLine("Circle","Westminster","St. James's Park");
        map.addTramLine("Circle","Embankment","Westminster");
        map.addTramLine("Circle","Temple","Embankment");
        map.addTramLine("Circle","Blackfriars","Temple");
        map.addTramLine("Circle","Mansion House","Blackfriars");
        map.addTramLine("Circle","Cannon Street","Mansion House");
        map.addTramLine("Circle","Gloucester Road","High Street Kensington");
        map.addTramLine("Circle","Notting Hill Gate","Bayswater");
        map.addTramLine("Circle","Bayswater","Paddington");
        map.addTramLine("Circle","Westbourne Park","Royal Oak");
        map.addTramLine("Circle","Latimer Road","Ladbroke Grove");
        map.addTramLine("Circle","Wood Lane","Latimer Road");
        map.addTramLine("Circle","Shepherd's Bush Market","Wood Lane");
        map.addTramLine("Circle","Royal Oak","Paddington");
        map.addTramLine("Circle","Goldhawk Road","Shepherd's Bush Market");
        map.addTramLine("Circle","Hammersmith","Goldhawk Road");
        map.addTramLine("Circle","Ladbroke Grove","Westbourne Park");
        map.addTramLine("Circle","Paddington","Edgware Road (Hammersmith)");
//        map.addTramLine("District","Gunnersbury","Kew Gardens");
//        map.addTramLine("District","Kew Gardens","Richmond");
//        map.addTramLine("District","Turnham Green","Chiswick Park");
//        map.addTramLine("District","Chiswick Park","Acton Town");
//        map.addTramLine("District","Acton Town","Ealing Common");
//        map.addTramLine("District","Ealing Common","Ealing Broadway");
//        map.addTramLine("District","Upney","Barking");
//        map.addTramLine("District","Dagenham Heathway","Becontree");
//        map.addTramLine("District","Dagenham East","Dagenham Heathway");
//        map.addTramLine("District","Elm Park","Dagenham East");
//        map.addTramLine("District","Hornchurch","Elm Park");
//        map.addTramLine("District","Fulham Broadway","Parsons Green");
//        map.addTramLine("District","Upminster Bridge","Hornchurch");
//        map.addTramLine("District","Upminster","Upminster Bridge");
//        map.addTramLine("District","Stamford Brook","Turnham Green");
//        map.addTramLine("District","Ravenscourt Park","Stamford Brook");
//        map.addTramLine("District","East Putney","Southfields");
//        map.addTramLine("District","Putney Bridge","East Putney");
//        map.addTramLine("District","Parsons Green","Putney Bridge");
//        map.addTramLine("District","West Brompton","Fulham Broadway");
//        map.addTramLine("District","Earl's Court","West Brompton");
//        map.addTramLine("District","Earl's Court","Kensington (Olympia)");
//        map.addTramLine("District","Bayswater","Paddington");
//        map.addTramLine("District","Notting Hill Gate","Bayswater");
//        map.addTramLine("District","Wimbledon Park","Wimbledon");
//        map.addTramLine("District","Turnham Green","Gunnersbury");
//        map.addTramLine("District","Paddington","Edgware Road (Hammersmith)");
//        map.addTramLine("District","Earl's Court","West Kensington");
//        map.addTramLine("District","West Kensington","Barons Court");
//        map.addTramLine("District","Barons Court","Hammersmith");
//        map.addTramLine("District","Hammersmith","Ravenscourt Park");
//        map.addTramLine("District","Southfields","Wimbledon Park");
//        map.addTramLine("District","Whitechapel","Aldgate East");
//        map.addTramLine("District","South Kensington","Gloucester Road");
//        map.addTramLine("District","Gloucester Road","Earl's Court");
//        map.addTramLine("District","Bow Church","Stepney Green");
//        map.addTramLine("District","Earl's Court","High Street Kensington");
//        map.addTramLine("District","High Street Kensington","Notting Hill Gate");
//        map.addTramLine("District","Mansion House","Blackfriars");
//        map.addTramLine("District","Becontree","Upney");
//        map.addTramLine("District","Monument","Cannon Street");
//        map.addTramLine("District","Tower Hill","Monument");
//        map.addTramLine("District","Aldgate East","Tower Hill");
//        map.addTramLine("District","Stepney Green","Whitechapel");
//        map.addTramLine("District","Sloane Square","South Kensington");
//        map.addTramLine("District","Victoria","Sloane Square");
//        map.addTramLine("District","St. James's Park","Victoria");
//        map.addTramLine("District","Barking","East Ham");
//        map.addTramLine("District","East Ham","Upton Park");
//        map.addTramLine("District","Upton Park","Plaistow");
//        map.addTramLine("District","Plaistow","West Ham");
//        map.addTramLine("District","West Ham","Bromley-by-Bow");
//        map.addTramLine("District","Bromley-by-Bow","Bow Road");
//        map.addTramLine("District","Bow Road","Bow Church");
//        map.addTramLine("District","Westminster","St. James's Park");
//        map.addTramLine("District","Cannon Street","Mansion House");
//        map.addTramLine("District","Blackfriars","Temple");
//        map.addTramLine("District","Temple","Embankment");
//        map.addTramLine("District","Embankment","Westminster");
//        map.addTramLine("DLR","Langdon Park","All Saints");
//        map.addTramLine("DLR","All Saints","Poplar");
//        map.addTramLine("DLR","Poplar","Westferry");
//        map.addTramLine("DLR","Westferry","Limehouse");
//        map.addTramLine("DLR","Limehouse","Shadwell");
//        map.addTramLine("DLR","Shadwell","Tower Gateway");
//        map.addTramLine("DLR","Gallions Reach","Beckton");
//        map.addTramLine("DLR","Devons Road","Langdon Park");
//        map.addTramLine("DLR","Bow Church","Devons Road");
//        map.addTramLine("DLR","Stratford","Pudding Mill Lane");
//        map.addTramLine("DLR","Shadwell","Bank");
//        map.addTramLine("DLR","Canning Town","West Silvertown");
//        map.addTramLine("DLR","Pudding Mill Lane","Bow Church");
//        map.addTramLine("DLR","Poplar","West India Quay");
//        map.addTramLine("DLR","London City Airport","King George V");
//        map.addTramLine("DLR","Pontoon Dock","London City Airport");
//        map.addTramLine("DLR","West Silvertown","Pontoon Dock");
//        map.addTramLine("DLR","Star Lane","Canning Town");
//        map.addTramLine("DLR","Abbey Road","West Ham");
//        map.addTramLine("DLR","Stratford High Street","Abbey Road");
//        map.addTramLine("DLR","Stratford","Stratford High Street");
//        map.addTramLine("DLR","Cyprus","Gallions Reach");
//        map.addTramLine("DLR","Beckton Park","Cyprus");
//        map.addTramLine("DLR","Royal Albert","Beckton Park");
//        map.addTramLine("DLR","Canning Town","Royal Victoria");
//        map.addTramLine("DLR","West Ham","Star Lane");
//        map.addTramLine("DLR","Prince Regent","Royal Albert");
//        map.addTramLine("DLR","Royal Victoria","Custom House");
//        map.addTramLine("DLR","East India","Canning Town");
//        map.addTramLine("DLR","King George V","Woolwich Arsenal");
//        map.addTramLine("DLR","West India Quay","Canary Wharf");
//        map.addTramLine("DLR","Canary Wharf","Heron Quays");
//        map.addTramLine("DLR","Heron Quays","South Quay");
//        map.addTramLine("DLR","South Quay","Crossharbour And London Arena");
//        map.addTramLine("DLR","Crossharbour And London Arena","Mudchute");
//        map.addTramLine("DLR","Mudchute","Island Gardens");
//        map.addTramLine("DLR","Island Gardens","Cutty Sark for Maritime Greenwich");
//        map.addTramLine("DLR","Cutty Sark for Maritime Greenwich","Greenwich");
//        map.addTramLine("DLR","Greenwich","Deptford Bridge");
//        map.addTramLine("DLR","Blackwall","East India");
//        map.addTramLine("DLR","Poplar","Blackwall");
//        map.addTramLine("DLR","Elverson Road","Lewisham");
//        map.addTramLine("DLR","Deptford Bridge","Elverson Road");
//        map.addTramLine("DLR","Custom House","Prince Regent");
        map.addTramLine("Hammersmith And City","Mile End","Bow Road");
        map.addTramLine("Hammersmith And City","Plaistow","Upton Park");
        map.addTramLine("Hammersmith And City","Hammersmith","Goldhawk Road");
        map.addTramLine("Hammersmith And City","Goldhawk Road","Shepherd's Bush Market");
        map.addTramLine("Hammersmith And City","Shepherd's Bush Market","Wood Lane");
        map.addTramLine("Hammersmith And City","Royal Oak","Paddington");
        map.addTramLine("Hammersmith And City","Wood Lane","Latimer Road");
        map.addTramLine("Hammersmith And City","Latimer Road","Ladbroke Grove");
        map.addTramLine("Hammersmith And City","Ladbroke Grove","Westbourne Park");
        map.addTramLine("Hammersmith And City","Liverpool Street","Aldgate East");
        map.addTramLine("Hammersmith And City","Aldgate East","Whitechapel");
        map.addTramLine("Hammersmith And City","Whitechapel","Stepney Green");
        map.addTramLine("Hammersmith And City","Stepney Green","Mile End");
        map.addTramLine("Hammersmith And City","Bow Road","Bromley-by-Bow");
        map.addTramLine("Hammersmith And City","Bromley-by-Bow","West Ham");
        map.addTramLine("Hammersmith And City","West Ham","Plaistow");
        map.addTramLine("Hammersmith And City","Upton Park","East Ham");
        map.addTramLine("Hammersmith And City","Moorgate","Liverpool Street");
        map.addTramLine("Hammersmith And City","Barbican","Moorgate");
        map.addTramLine("Hammersmith And City","Westbourne Park","Royal Oak");
        map.addTramLine("Hammersmith And City","Paddington","Edgware Road (Hammersmith)");
        map.addTramLine("Hammersmith And City","Edgware Road (Hammersmith)","Baker Street");
        map.addTramLine("Hammersmith And City","Baker Street","Great Portland Street");
        map.addTramLine("Hammersmith And City","Great Portland Street","Euston Square");
        map.addTramLine("Hammersmith And City","Euston Square","King's Cross St. Pancras");
        map.addTramLine("Hammersmith And City","King's Cross St. Pancras","Farringdon");
        map.addTramLine("Hammersmith And City","Farringdon","Barbican");
        map.addTramLine("Hammersmith And City","East Ham","Barking");
//        map.addTramLine("Jubilee","Stanmore","Canons Park");
//        map.addTramLine("Jubilee","Canada Water","Canary Wharf");
//        map.addTramLine("Jubilee","West Hampstead","Finchley Road");
//        map.addTramLine("Jubilee","Kilburn","West Hampstead");
//        map.addTramLine("Jubilee","Willesden Green","Kilburn");
//        map.addTramLine("Jubilee","Dollis Hill","Willesden Green");
//        map.addTramLine("Jubilee","Neasden","Dollis Hill");
//        map.addTramLine("Jubilee","Wembley Park","Neasden");
//        map.addTramLine("Jubilee","King'sbury","Wembley Park");
//        map.addTramLine("Jubilee","Queen'sbury","King'sbury");
//        map.addTramLine("Jubilee","Canons Park","Queen'sbury");
//        map.addTramLine("Jubilee","Bermondsey","Canada Water");
//        map.addTramLine("Jubilee","Swiss Cottage","St. Johns Wood");
//        map.addTramLine("Jubilee","St. Johns Wood","Baker Street");
//        map.addTramLine("Jubilee","Finchley Road","Swiss Cottage");
//        map.addTramLine("Jubilee","Canary Wharf","North Greenwich");
//        map.addTramLine("Jubilee","North Greenwich","Canning Town");
//        map.addTramLine("Jubilee","Canning Town","West Ham");
//        map.addTramLine("Jubilee","West Ham","Stratford");
//        map.addTramLine("Jubilee","London Bridge","Bermondsey");
//        map.addTramLine("Jubilee","Southwark","London Bridge");
//        map.addTramLine("Jubilee","Waterloo","Southwark");
//        map.addTramLine("Jubilee","Westminster","Waterloo");
//        map.addTramLine("Jubilee","Green Park","Westminster");
//        map.addTramLine("Jubilee","Bond Street","Green Park");
//        map.addTramLine("Jubilee","Baker Street","Bond Street");
        map.addTramLine("Metropolitan","Great Portland Street","Baker Street");
        map.addTramLine("Metropolitan","Ruislip","Ickenham");
        map.addTramLine("Metropolitan","Aldgate","Liverpool Street");
        map.addTramLine("Metropolitan","Liverpool Street","Moorgate");
        map.addTramLine("Metropolitan","Moorgate","Barbican");
        map.addTramLine("Metropolitan","Ickenham","Hillingdon");
        map.addTramLine("Metropolitan","Ruislip Manor","Ruislip");
        map.addTramLine("Metropolitan","Eastcote","Ruislip Manor");
        map.addTramLine("Metropolitan","West Harrow","Eastcote");
        map.addTramLine("Metropolitan","Harrow-on-the-Hill","West Harrow");
        map.addTramLine("Metropolitan","Farringdon","King's Cross St. Pancras");
        map.addTramLine("Metropolitan","Northwick Park","Harrow-on-the-Hill");
        map.addTramLine("Metropolitan","Preston Road","Northwick Park");
        map.addTramLine("Metropolitan","Wembley Park","Preston Road");
        map.addTramLine("Metropolitan","Finchley Road","Wembley Park");
        map.addTramLine("Metropolitan","Baker Street","Finchley Road");
        map.addTramLine("Metropolitan","Euston Square","Great Portland Street");
        map.addTramLine("Metropolitan","Hillingdon","Uxbridge");
        map.addTramLine("Metropolitan","Harrow-on-the-Hill","North Harrow");
        map.addTramLine("Metropolitan","North Harrow","Pinner");
        map.addTramLine("Metropolitan","Barbican","Farringdon");
        map.addTramLine("Metropolitan","Chalfont And Latimer","Amersham");
        map.addTramLine("Metropolitan","Chalfont And Latimer","Chesham");
        map.addTramLine("Metropolitan","Chorleywood","Chalfont And Latimer");
        map.addTramLine("Metropolitan","Rickmansworth","Chorleywood");
        map.addTramLine("Metropolitan","Moor Park","Rickmansworth");
        map.addTramLine("Metropolitan","King's Cross St. Pancras","Euston Square");
        map.addTramLine("Metropolitan","Croxley","Watford");
        map.addTramLine("Metropolitan","Pinner","Northwood Hills");
        map.addTramLine("Metropolitan","Moor Park","Croxley");
        map.addTramLine("Metropolitan","Northwood Hills","Moor Park");
//        map.addTramLine("Northern","Clapham Common","Clapham North");
//        map.addTramLine("Northern","Clapham North","Stockwell");
//        map.addTramLine("Northern","Stockwell","Oval");
//        map.addTramLine("Northern","Goodge Street","Warren Street");
//        map.addTramLine("Northern","Oval","Kennington");
//        map.addTramLine("Northern","Clapham South","Clapham Common");
//        map.addTramLine("Northern","Tooting Bec","Balham");
//        map.addTramLine("Northern","Tooting Broadway","Tooting Bec");
//        map.addTramLine("Northern","Colliers Wood","Tooting Broadway");
//        map.addTramLine("Northern","South Wimbledon","Colliers Wood");
//        map.addTramLine("Northern","Morden","South Wimbledon");
//        map.addTramLine("Northern","Tufnell Park","Archway");
//        map.addTramLine("Northern","Totteridge And Whetstone","High Barnet");
//        map.addTramLine("Northern","Kennington","Elephant And Castle");
//        map.addTramLine("Northern","Camden Town","Chalk Farm");
//        map.addTramLine("Northern","Chalk Farm","Belsize Park");
//        map.addTramLine("Northern","Belsize Park","Hampstead");
//        map.addTramLine("Northern","Hampstead","Golders Green");
//        map.addTramLine("Northern","Brent Cross","Hendon Central");
//        map.addTramLine("Northern","Colindale","Burnt Oak");
//        map.addTramLine("Northern","Burnt Oak","Edgware");
//        map.addTramLine("Northern","Camden Town","Kentish Town");
//        map.addTramLine("Northern","Kentish Town","Tufnell Park");
//        map.addTramLine("Northern","Archway","Highgate");
//        map.addTramLine("Northern","Highgate","East Finchley");
//        map.addTramLine("Northern","East Finchley","Mill Hill East");
//        map.addTramLine("Northern","East Finchley","Finchley Central");
//        map.addTramLine("Northern","Finchley Central","West Finchley");
//        map.addTramLine("Northern","West Finchley","Woodside Park");
//        map.addTramLine("Northern","Euston","Camden Town");
//        map.addTramLine("Northern","Mornington Crescent","Camden Town");
//        map.addTramLine("Northern","Euston","Mornington Crescent");
//        map.addTramLine("Northern","Elephant And Castle","Borough");
//        map.addTramLine("Northern","Borough","London Bridge");
//        map.addTramLine("Northern","London Bridge","Bank");
//        map.addTramLine("Northern","Bank","Moorgate");
//        map.addTramLine("Northern","Moorgate","Old Street");
//        map.addTramLine("Northern","Old Street","Angel");
//        map.addTramLine("Northern","Angel","King's Cross St. Pancras");
//        map.addTramLine("Northern","King's Cross St. Pancras","Euston");
//        map.addTramLine("Northern","Kennington","Waterloo");
//        map.addTramLine("Northern","Waterloo","Embankment");
//        map.addTramLine("Northern","Embankment","Charing Cross");
//        map.addTramLine("Northern","Charing Cross","Leicester Square");
//        map.addTramLine("Northern","Leicester Square","Tottenham Court Road");
//        map.addTramLine("Northern","Tottenham Court Road","Goodge Street");
//        map.addTramLine("Northern","Warren Street","Euston");
//        map.addTramLine("Northern","Woodside Park","Totteridge And Whetstone");
//        map.addTramLine("Northern","Balham","Clapham South");
//        map.addTramLine("Northern","Hendon Central","Colindale");
//        map.addTramLine("Northern","Golders Green","Brent Cross");
//        map.addTramLine("Overground","Surrey Quays","New Cross Gate");
//        map.addTramLine("Overground","New Cross Gate","Brockley");
//        map.addTramLine("Overground","Brockley","Honor Oak Park");
//        map.addTramLine("Overground","Willesden Junction","Kensal Rise");
//        map.addTramLine("Overground","Honor Oak Park","Forest Hill");
//        map.addTramLine("Overground","Queen's Park","Kilburn High Road");
//        map.addTramLine("Overground","Forest Hill","Sydenham");
//        map.addTramLine("Overground","Surrey Quays","New Cross");
//        map.addTramLine("Overground","Canada Water","Surrey Quays");
//        map.addTramLine("Overground","Rotherhithe","Canada Water");
//        map.addTramLine("Overground","Wapping","Rotherhithe");
//        map.addTramLine("Overground","Shadwell","Wapping");
//        map.addTramLine("Overground","Whitechapel","Shadwell");
//        map.addTramLine("Overground","Shoreditch High Street","Whitechapel");
//        map.addTramLine("Overground","Hoxton","Shoreditch High Street");
//        map.addTramLine("Overground","Haggerston","Hoxton");
//        map.addTramLine("Overground","Dalston Junction","Haggerston");
//        map.addTramLine("Overground","South Kenton","North Wembley");
//        map.addTramLine("Overground","Penge West","Anerley");
//        map.addTramLine("Overground","North Wembley","Wembley Central");
//        map.addTramLine("Overground","Wembley Central","Stonebridge Park");
//        map.addTramLine("Overground","Stonebridge Park","Harlesden");
//        map.addTramLine("Overground","Harlesden","Willesden Junction");
//        map.addTramLine("Overground","Willesden Junction","Kensal Green");
//        map.addTramLine("Overground","Kensal Green","Queen's Park");
//        map.addTramLine("Overground","Wandsworth Road","Clapham Junction");
//        map.addTramLine("Overground","Clapham High Street","Wandsworth Road");
//        map.addTramLine("Overground","Sydenham","Penge West");
//        map.addTramLine("Overground","Denmark Hill","Clapham High Street");
//        map.addTramLine("Overground","Peckham Rye","Denmark Hill");
//        map.addTramLine("Overground","Queen's Road Peckham","Peckham Rye");
//        map.addTramLine("Overground","Surrey Quays","Queen's Road Peckham");
//        map.addTramLine("Overground","Sydenham","Crystal Palace");
//        map.addTramLine("Overground","Norwood Junction","West Croydon");
//        map.addTramLine("Overground","Anerley","Norwood Junction");
//        map.addTramLine("Overground","Canonbury","Dalston Junction");
//        map.addTramLine("Overground","Highbury And Islington","Canonbury");
//        map.addTramLine("Overground","Woodgrange Park","Barking");
//        map.addTramLine("Overground","Caledonian Road And Barnsbury","Highbury And Islington");
//        map.addTramLine("Overground","Camden Road","Caledonian Road And Barnsbury");
//        map.addTramLine("Overground","Kentish Town West","Camden Road");
//        map.addTramLine("Overground","Gospel Oak","Kentish Town West");
//        map.addTramLine("Overground","Hampstead Heath","Gospel Oak");
//        map.addTramLine("Overground","Finchley Road And Frognal","Hampstead Heath");
//        map.addTramLine("Overground","West Hampstead","Finchley Road And Frognal");
//        map.addTramLine("Overground","Brondesbury","West Hampstead");
//        map.addTramLine("Overground","Brondesbury Park","Brondesbury");
//        map.addTramLine("Overground","Kensal Rise","Brondesbury Park");
//        map.addTramLine("Overground","Gospel Oak","Upper Holloway");
//        map.addTramLine("Overground","Acton Central","Willesden Junction");
//        map.addTramLine("Overground","South Acton","Acton Central");
//        map.addTramLine("Overground","Gunnersbury","South Acton");
//        map.addTramLine("Overground","Kew Gardens","Gunnersbury");
//        map.addTramLine("Overground","Richmond","Kew Gardens");
//        map.addTramLine("Overground","Canonbury","Dalston Kingsland");
//        map.addTramLine("Overground","Dalston Kingsland","Hackney Central");
//        map.addTramLine("Overground","Hackney Central","Homerton");
//        map.addTramLine("Overground","Wanstead Park","Woodgrange Park");
//        map.addTramLine("Overground","Leytonstone High Road","Wanstead Park");
//        map.addTramLine("Overground","Leyton Midland Road","Leytonstone High Road");
//        map.addTramLine("Overground","Blackhorse Road","Walthamstow Queen's Road");
//        map.addTramLine("Overground","South Tottenham","Blackhorse Road");
//        map.addTramLine("Overground","Harringay Green Lanes","South Tottenham");
//        map.addTramLine("Overground","Crouch Hill","Harringay Green Lanes");
//        map.addTramLine("Overground","Upper Holloway","Crouch Hill");
//        map.addTramLine("Overground","Imperial Wharf","Clapham Junction");
//        map.addTramLine("Overground","West Brompton","Imperial Wharf");
//        map.addTramLine("Overground","Kensington (Olympia)","West Brompton");
//        map.addTramLine("Overground","Shepherd's Bush","Kensington (Olympia)");
//        map.addTramLine("Overground","Willesden Junction","Shepherd's Bush");
//        map.addTramLine("Overground","Kilburn High Road","South Hampstead");
//        map.addTramLine("Overground","Hackney Wick","Stratford");
//        map.addTramLine("Overground","Homerton","Hackney Wick");
//        map.addTramLine("Overground","South Hampstead","Euston");
//        map.addTramLine("Overground","Kenton","South Kenton");
//        map.addTramLine("Overground","Harrow And Wealdstone","Kenton");
//        map.addTramLine("Overground","Walthamstow Queen's Road","Leyton Midland Road");
//        map.addTramLine("Overground","Watford Junction","Watford High Street");
//        map.addTramLine("Overground","Watford High Street","Bushey");
//        map.addTramLine("Overground","Bushey","Carpenders Park");
//        map.addTramLine("Overground","Carpenders Park","Hatch End");
//        map.addTramLine("Overground","Hatch End","Headstone Lane");
//        map.addTramLine("Overground","Headstone Lane","Harrow And Wealdstone");
//        map.addTramLine("Piccadilly","Acton Town","South Ealing");
//        map.addTramLine("Piccadilly","South Ealing","Northfields");
//        map.addTramLine("Piccadilly","Northfields","Boston Manor");
//        map.addTramLine("Piccadilly","Boston Manor","Osterley");
//        map.addTramLine("Piccadilly","Osterley","Hounslow East");
//        map.addTramLine("Piccadilly","Hounslow East","Hounslow Central");
//        map.addTramLine("Piccadilly","Turnham Green","Acton Town");
//        map.addTramLine("Piccadilly","Hammersmith","Turnham Green");
//        map.addTramLine("Piccadilly","Barons Court","Hammersmith");
//        map.addTramLine("Piccadilly","Earl's Court","Barons Court");
//        map.addTramLine("Piccadilly","Gloucester Road","Earl's Court");
//        map.addTramLine("Piccadilly","South Kensington","Gloucester Road");
//        map.addTramLine("Piccadilly","Knightsbridge","South Kensington");
//        map.addTramLine("Piccadilly","Ickenham","Hillingdon");
//        map.addTramLine("Piccadilly","Hounslow Central","Hounslow West");
//        map.addTramLine("Piccadilly","Hounslow West","Hatton Cross");
//        map.addTramLine("Piccadilly","Hatton Cross","Heathrow Terminals 1, 2, 3");
//        map.addTramLine("Piccadilly","Hillingdon","Uxbridge");
//        map.addTramLine("Piccadilly","Ruislip","Ickenham");
//        map.addTramLine("Piccadilly","Ruislip Manor","Ruislip");
//        map.addTramLine("Piccadilly","Eastcote","Ruislip Manor");
//        map.addTramLine("Piccadilly","Rayners Lane","Eastcote");
//        map.addTramLine("Piccadilly","Sudbury Hill","Rayners Lane");
//        map.addTramLine("Piccadilly","Sudbury Town","Sudbury Hill");
//        map.addTramLine("Piccadilly","Alperton","Sudbury Town");
//        map.addTramLine("Piccadilly","North Ealing","Park Royal");
//        map.addTramLine("Piccadilly","Ealing Common","North Ealing");
//        map.addTramLine("Piccadilly","Acton Town","Ealing Common");
//        map.addTramLine("Piccadilly","Heathrow Terminals 1, 2, 3","Heathrow Terminal 5");
//        map.addTramLine("Piccadilly","Heathrow Terminals 1, 2, 3","Heathrow Terminal 4");
//        map.addTramLine("Piccadilly","Hatton Cross","Heathrow Terminal 4");
//        map.addTramLine("Piccadilly","Hyde Park Corner","Knightsbridge");
//        map.addTramLine("Piccadilly","Green Park","Hyde Park Corner");
//        map.addTramLine("Piccadilly","Piccadilly Circus","Green Park");
//        map.addTramLine("Piccadilly","Turnpike Lane","Manor House");
//        map.addTramLine("Piccadilly","Wood Green","Turnpike Lane");
//        map.addTramLine("Piccadilly","Bounds Green","Wood Green");
//        map.addTramLine("Piccadilly","Arnos Grove","Bounds Green");
//        map.addTramLine("Piccadilly","Southgate","Arnos Grove");
//        map.addTramLine("Piccadilly","Oakwood","Southgate");
//        map.addTramLine("Piccadilly","Cockfosters","Oakwood");
//        map.addTramLine("Piccadilly","Park Royal","Alperton");
//        map.addTramLine("Piccadilly","Manor House","Finsbury Park");
//        map.addTramLine("Piccadilly","Finsbury Park","Arsenal");
//        map.addTramLine("Piccadilly","Leicester Square","Piccadilly Circus");
//        map.addTramLine("Piccadilly","Covent Garden","Leicester Square");
//        map.addTramLine("Piccadilly","Holborn","Covent Garden");
//        map.addTramLine("Piccadilly","Russell Square","Holborn");
//        map.addTramLine("Piccadilly","King's Cross St. Pancras","Russell Square");
//        map.addTramLine("Piccadilly","Caledonian Road","King's Cross St. Pancras");
//        map.addTramLine("Piccadilly","Holloway Road","Caledonian Road");
//        map.addTramLine("Piccadilly","Arsenal","Holloway Road");
//        map.addTramLine("Victoria","King's Cross St. Pancras","Warren Street");
//        map.addTramLine("Victoria","Highbury And Islington","King's Cross St. Pancras");
//        map.addTramLine("Victoria","Finsbury Park","Highbury And Islington");
//        map.addTramLine("Victoria","Seven Sisters","Finsbury Park");
//        map.addTramLine("Victoria","Tottenham Hale","Seven Sisters");
//        map.addTramLine("Victoria","Warren Street","Oxford Circus");
//        map.addTramLine("Victoria","Oxford Circus","Green Park");
//        map.addTramLine("Victoria","Walthamstow Central","Blackhorse Road");
//        map.addTramLine("Victoria","Green Park","Victoria");
//        map.addTramLine("Victoria","Victoria","Pimlico");
//        map.addTramLine("Victoria","Pimlico","Vauxhall");
//        map.addTramLine("Victoria","Vauxhall","Stockwell");
//        map.addTramLine("Victoria","Stockwell","Brixton");
//        map.addTramLine("Victoria","Blackhorse Road","Tottenham Hale");
//        map.addTramLine("Waterloo And City","Waterloo","Bank");
    }
}
